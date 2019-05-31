package vmodev.clearkeep.activities

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import dagger.android.support.DaggerAppCompatActivity
import im.vector.R
import im.vector.databinding.ActivityMessageListBinding
import vmodev.clearkeep.activities.interfaces.IMessageListActivity
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.factories.viewmodels.interfaces.IMessageListActivityViewModelFactory
import javax.inject.Inject

class MessageListActivity : DaggerAppCompatActivity(), IMessageListActivity {

    @Inject
    lateinit var viewModelFactory: IMessageListActivityViewModelFactory;

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
        binding.lifecycleOwner = this;
        viewModelFactory.getViewModel().setRoomIdForGetListMessage(roomId);
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val ROOM_ID = "ROOM_ID";
    }
}
