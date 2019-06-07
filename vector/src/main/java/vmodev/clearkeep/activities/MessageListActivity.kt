package vmodev.clearkeep.activities

import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.util.DiffUtil
import android.util.Log
import dagger.android.support.DaggerAppCompatActivity
import im.vector.Matrix
import im.vector.R
import im.vector.databinding.ActivityMessageListBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.activities.interfaces.IMessageListActivity
import vmodev.clearkeep.adapters.ListMessageRecyclerViewAdapter
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IMessageListActivityViewModelFactory
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.User
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MessageListActivity : DaggerAppCompatActivity(), IMessageListActivity {

    @Inject
    lateinit var viewModelFactory: IMessageListActivityViewModelFactory;
    @Inject
    lateinit var appExecutors: AppExecutors;

    private lateinit var binding: ActivityMessageListBinding;
    private val dataBindingComponent: ActivityDataBindingComponent = ActivityDataBindingComponent(this);
    private lateinit var roomId: String;

    private lateinit var session: MXSession;
    private val members = HashMap<String, User>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message_list, dataBindingComponent);
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
        viewModelFactory.getViewModel().getRoomResult().observe(this, Observer {
            it?.data?.let {
                Log.d("RoomChange", it.avatarUrl)
            }
        })
        val adapter = ListMessageRecyclerViewAdapter(session.myUserId, members, appExecutors, dataBindingComponent, object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(p0: Message, p1: Message): Boolean {
                return p0.id == p1.id;
            }

            override fun areContentsTheSame(p0: Message, p1: Message): Boolean {
                return p0.encryptedContent == p1.encryptedContent;
            }
        });
        binding.recyclerViewListMessage.adapter = adapter;
        binding.messagesUpdate = viewModelFactory.getViewModel().registerMatrixMessageHandlerResult();
        viewModelFactory.getViewModel().getUsersByRoomIdResult().observe(this, Observer {
            it?.data?.let {
                it.forEach { t: User? ->
                    t?.let {
                        if (!members.containsKey(it.id))
                            members.put(it.id, it);
                    }
                }
            }
        })
        viewModelFactory.getViewModel().getListMessageResult().observe(this, Observer {
            adapter.submitList(it?.data);
            it?.data?.let {
                Log.d("List Size", it.size.toString());
                if (it.isNotEmpty())
                    binding.recyclerViewListMessage.smoothScrollToPosition(it.size - 1);
            }
        })
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
