package vmodev.clearkeep.activities

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.util.DiffUtil
import android.util.Log
import dagger.android.support.DaggerAppCompatActivity
import im.vector.R
import im.vector.databinding.ActivityMessageListBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import vmodev.clearkeep.activities.interfaces.IMessageListActivity
import vmodev.clearkeep.adapters.ListMessageRecyclerViewAdapter
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IMessageListActivityViewModelFactory
import vmodev.clearkeep.viewmodelobjects.Message
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message_list, dataBindingComponent);
        roomId = intent.getStringExtra(ROOM_ID);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setTitle(R.string.edit_profile);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed();
        }
        binding.messages = viewModelFactory.getViewModel().getListMessageResult();
        val adapter = ListMessageRecyclerViewAdapter(appExecutors, dataBindingComponent, object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(p0: Message, p1: Message): Boolean {
                return p0.messageId == p1.messageId;
            }

            override fun areContentsTheSame(p0: Message, p1: Message): Boolean {
                return p0.encryptedContent == p1.encryptedContent;
            }
        });
        binding.recyclerViewListMessage.adapter = adapter;
        viewModelFactory.getViewModel().getListMessageResult().observe(this, Observer {
            adapter.submitList(it?.data);
            it?.data?.let {
                if (it.size > 0) {
                    Log.d("Message Data", it[0].encryptedContent)
                }
            }
        })
        binding.message = viewModelFactory.getViewModel().getMessageResult();
        viewModelFactory.getViewModel().getMessageResult().observe(this, Observer {
            it?.data?.let {
                Log.d("Message Data", it.encryptedContent)
            }
        })
        binding.lifecycleOwner = this;
        viewModelFactory.getViewModel().setRoomIdForGetListMessage(roomId);
        Observable.timer(10, TimeUnit.SECONDS).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            Log.d("Message Start", "Start Find");
            viewModelFactory.getViewModel().setRoomIdForGetListMessage(roomId);
            viewModelFactory.getViewModel().setMessageId("\$1559563435113OVimK:study.sinbadflyce.com")
        }
        Log.d("Message Room", roomId);
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val ROOM_ID = "ROOM_ID";
    }
}
