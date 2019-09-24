package vmodev.clearkeep.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import im.vector.R
import im.vector.databinding.ActivityRoomSettingsBinding
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.InviteUsersToRoomFragment
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomSettingsActivityViewModel
import javax.inject.Inject

class RoomSettingsActivity : DataBindingDaggerActivity(), IActivity {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractRoomSettingsActivityViewModel>;

    lateinit var roomId: String;

    lateinit var binding: ActivityRoomSettingsBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_room_settings, dataBinding.getDataBindingComponent());
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        binding.toolbar.setNavigationOnClickListener { v ->
            onBackPressed();
        }

        setupButton();
        binding.room = viewModelFactory.getViewModel().getRoom();
        binding.user = viewModelFactory.getViewModel().getUserResult();
        viewModelFactory.getViewModel().getRoom().observe(this, Observer {
            it?.data?.userCreated?.let {
                viewModelFactory.getViewModel().setUserId(it)
            }
        })
        binding.lifecycleOwner = this;
        roomId = intent.getStringExtra(ROOM_ID);
        viewModelFactory.getViewModel().setRoomId(roomId);
    }

    private fun setupButton() {
        binding.leaveRoomGroup.setOnClickListener { v ->
            binding.leaveRoom = viewModelFactory.getViewModel().getLeaveRoom();
            viewModelFactory.getViewModel().getLeaveRoom().observe(this, Observer { t ->
                t?.let { resource ->
                    if (resource.status == Status.SUCCESS) {
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                }
            })
            viewModelFactory.getViewModel().setLeaveRoom(roomId);
        }
        binding.settingsGroup.setOnClickListener { v ->
            val securityIntent = Intent(this, OtherRoomSettingsActivity::class.java);
            securityIntent.putExtra(OtherRoomSettingsActivity.ROOM_ID, roomId);
            startActivity(securityIntent);
        }
        binding.membersGroup.setOnClickListener { v ->
            val usersIntent = Intent(this, RoomMemberListActivity::class.java);
            usersIntent.putExtra(RoomMemberListActivity.ROOM_ID, roomId);
            startActivity(usersIntent);
        }
        binding.addPeopleGroup.setOnClickListener { v ->
//            val intentAddPeople = Intent(this, InviteUsersToRoomFragment::class.java);
//            intentAddPeople.putExtra(InviteUsersToRoomFragment.ROOM_ID, roomId);
//            intentAddPeople.putExtra(InviteUsersToRoomFragment.CREATE_FROM_NEW_ROOM, false);
//            startActivity(intentAddPeople);
        }
        binding.filesGroup.setOnClickListener { v ->
            val filesIntent = Intent(this, RoomfilesListActivity::class.java)
            filesIntent.putExtra(RoomfilesListActivity.ROOM_ID, roomId);
            startActivity(filesIntent)
        }
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    companion object {
        const val ROOM_ID = "ROOM_ID";
    }
}
