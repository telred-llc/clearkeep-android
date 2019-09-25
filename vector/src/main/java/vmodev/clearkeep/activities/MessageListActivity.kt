package vmodev.clearkeep.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import im.vector.Matrix
import im.vector.R
import im.vector.databinding.ActivityMessageListBinding
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IMessageListActivity
import vmodev.clearkeep.adapters.ListMessageRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IMessageListActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractMessageListActivityViewModel
import javax.inject.Inject

class MessageListActivity : DataBindingDaggerActivity(), IActivity {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractMessageListActivityViewModel>;
    @Inject
    lateinit var appExecutors: AppExecutors;

    private lateinit var binding: ActivityMessageListBinding;
    private lateinit var roomId: String;

    private lateinit var session: MXSession;
    private val members = HashMap<String, User>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message_list);
        roomId = intent.getStringExtra(ROOM_ID);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }
        binding.linearLayoutRoomSetting.setOnClickListener {
            val intentSetting = Intent(this, RoomSettingsActivity::class.java);
            intentSetting.putExtra(RoomSettingsActivity.ROOM_ID, roomId);
            startActivity(intentSetting);
        }
        session = Matrix.getInstance(applicationContext).defaultSession;
        binding.messages = viewModelFactory.getViewModel().getListMessageResult();
        binding.room = viewModelFactory.getViewModel().getRoomResult();
        val adapter = ListMessageRecyclerViewAdapter(session.myUserId, members, appExecutors, object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(p0: Message, p1: Message): Boolean {
                return p0.id == p1.id;
            }

            override fun areContentsTheSame(p0: Message, p1: Message): Boolean {
                return p0.encryptedContent == p1.encryptedContent;
            }
        });
        binding.recyclerViewListMessage.adapter = adapter;
        binding.messagesUpdate = viewModelFactory.getViewModel().registerMatrixMessageHandlerResult();
        viewModelFactory.getViewModel().getUsersByRoomIdResult()!!.observe(this, Observer {
            it?.data?.let {
                it.forEach { t: User? ->
                    t?.let {
                        if (!members.containsKey(it.id))
                            members.put(it.id, it);
                    }
                }
            }
        });
        viewModelFactory.getViewModel().getListMessageResult().observe(this, Observer {
            adapter.submitList(it?.data);
            it?.data?.let {
                if (it.isNotEmpty())
                    binding.recyclerViewListMessage.smoothScrollToPosition(it.size - 1);
            }
        });
        viewModelFactory.getViewModel().getSendMessageResult().observe(this, Observer {
            it?.data?.let {
                Log.d("Message", it.toString());
            }
        })
        binding.buttonSend.setOnClickListener {
            viewModelFactory.getViewModel().setSendMessage(roomId, binding.editTextMessageContent.text.toString());
            binding.editTextMessageContent.setText("");
        };
        binding.lifecycleOwner = this;
        viewModelFactory.getViewModel().setRoomIdForGetListMessage(roomId);
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelFactory.getViewModel().removeMatrixMessageHandler(roomId);
    }

    companion object {
        const val ROOM_ID = "ROOM_ID";
    }
}
