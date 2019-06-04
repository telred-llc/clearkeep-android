package vmodev.clearkeep.activities

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import dagger.android.support.DaggerAppCompatActivity
import im.vector.R
import im.vector.databinding.ActivityRoomSettingsBinding
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.RoomViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import javax.inject.Inject
import javax.inject.Named

class RoomSettingsActivity : DaggerAppCompatActivity(), IActivity {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;

    lateinit var roomViewModel: AbstractRoomViewModel;
    lateinit var roomId: String;

    private val dataBindingComponent: DataBindingComponent = ActivityDataBindingComponent(this);

    lateinit var binding: ActivityRoomSettingsBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_room_settings, dataBindingComponent);
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            onBackPressed();
        }
        roomViewModel = ViewModelProviders.of(this, viewModelFactory).get(AbstractRoomViewModel::class.java);
        setupButton();
        binding.room = roomViewModel.getRoom();
        binding.lifecycleOwner = this;
        roomId = intent.getStringExtra(ROOM_ID);
        roomViewModel.setRoomId(roomId);
    }

    private fun setupButton() {
        binding.leaveRoomGroup.setOnClickListener { v ->
            binding.leaveRoom = roomViewModel.getLeaveRoom();
            roomViewModel.getLeaveRoom().observe(this, Observer { t ->
                t?.let { resource ->
                    if (resource.status == Status.SUCCESS) {
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                }
            })
            roomViewModel.setLeaveRoom(roomId);
        }
        binding.settingsGroup.setOnClickListener { v ->
            val securityIntent = Intent(this, OtherRoomSettings::class.java);
            securityIntent.putExtra(OtherRoomSettings.ROOM_ID, roomId);
            startActivity(securityIntent);
        }
        binding.membersGroup.setOnClickListener { v ->
            val usersIntent = Intent(this, RoomMemberListActivity::class.java);
            usersIntent.putExtra(RoomMemberListActivity.ROOM_ID, roomId);
            startActivity(usersIntent);
        }
        binding.addPeopleGroup.setOnClickListener { v ->
            val intentAddPeople = Intent(this, InviteUsersToRoomActivity::class.java);
            intentAddPeople.putExtra(InviteUsersToRoomActivity.ROOM_ID, roomId);
            intentAddPeople.putExtra(InviteUsersToRoomActivity.CREATE_FROM_NEW_ROOM, false);
            startActivity(intentAddPeople);
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val ROOM_ID = "ROOM_ID";
    }
}
