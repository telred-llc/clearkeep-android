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
import android.support.v7.app.AlertDialog
import android.util.Log
import dagger.android.support.DaggerAppCompatActivity
import im.vector.R
import im.vector.databinding.ActivityRoomSettingsBinding
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.RoomViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomSettingsActivityViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import javax.inject.Inject
import javax.inject.Named

class RoomSettingsActivity : DataBindingDaggerActivity(), IActivity {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractRoomSettingsActivityViewModel>;

    lateinit var roomId: String;
    private var alertDialog : AlertDialog? = null
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
        binding.leaveRoomGroup.setOnClickListener {

            if (alertDialog==null) {
                alertDialog = AlertDialog.Builder(this).setTitle(R.string.leave_room)
                        .setMessage(R.string.do_you_want_leave_room)
                        .setNegativeButton(R.string.no, null)
                        .setPositiveButton(R.string.yes) { dialog, v ->
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

                        }.show()
            }
            if ( alertDialog!!.isShowing){
                Log.d("alertDialog","isShowing")
            }else{
                alertDialog!!.show()
            }

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
            val intentAddPeople = Intent(this, InviteUsersToRoomActivity::class.java);
            intentAddPeople.putExtra(InviteUsersToRoomActivity.ROOM_ID, roomId);
            intentAddPeople.putExtra(InviteUsersToRoomActivity.CREATE_FROM_NEW_ROOM, false);
            startActivity(intentAddPeople);
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
