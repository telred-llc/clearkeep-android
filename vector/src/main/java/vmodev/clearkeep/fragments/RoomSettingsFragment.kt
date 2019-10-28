package vmodev.clearkeep.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import im.vector.R
import im.vector.databinding.FragmentRoomSettingsBinding
import org.jetbrains.anko.colorAttr
import vmodev.clearkeep.activities.RoomfilesListActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomSettingsFragmentViewModel
import javax.inject.Inject

class RoomSettingsFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractRoomSettingsFragmentViewModel>;

    private var alertDialog: AlertDialog? = null
    private lateinit var binding: FragmentRoomSettingsBinding;
    private val args: RoomSettingsFragmentArgs by navArgs();

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_settings, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton();
        binding.room = viewModelFactory.getViewModel().getRoom();
        binding.user = viewModelFactory.getViewModel().getUserResult();
        viewModelFactory.getViewModel().getRoom().observe(this, Observer {
            it?.data?.userCreated?.let {
                viewModelFactory.getViewModel().setUserId(it)
            }
        })
        binding.lifecycleOwner = this;
        args.roomId?.let { viewModelFactory.getViewModel().setRoomId(it) }
        if(!binding.editTextRoomName.isFocusable){
            binding.editTextRoomName.colorAttr(ResourcesCompat.getColor(this.resources, R.color.color_grey, null))
        }
        if(!binding.editTextRoomTopic.isFocusable){
            binding.editTextRoomTopic.colorAttr(ResourcesCompat.getColor(this.resources, R.color.color_grey, null))
        }
    }

    private fun setupButton() {
        binding.leaveRoomGroup.setOnClickListener {

            if (alertDialog == null) {
                alertDialog = AlertDialog.Builder(this.context!!).setTitle(R.string.leave_room)
                        .setMessage(R.string.do_you_want_leave_room)
                        .setNegativeButton(R.string.no, null)
                        .setPositiveButton(R.string.yes) { dialog, v ->
                            binding.leaveRoom = viewModelFactory.getViewModel().getLeaveRoom();
                            viewModelFactory.getViewModel().getLeaveRoom().observe(this, Observer { t ->
                                t?.let { resource ->
                                    if (resource.status == Status.SUCCESS) {
                                        this.activity?.setResult(-1);
                                        this.activity?.finish();
                                    }
                                }
                            })
                            args.roomId?.let { viewModelFactory.getViewModel().setLeaveRoom(it); }

                        }.show()
            }
            if (alertDialog!!.isShowing) {
                Log.d("alertDialog", "isShowing")
            } else {
                alertDialog!!.show()
            }

        }
        binding.settingsGroup.setOnClickListener { v ->
            findNavController().navigate(RoomSettingsFragmentDirections.otherSettings().setRoomId(args.roomId));
        }
        binding.membersGroup.setOnClickListener { v ->
            findNavController().navigate(RoomSettingsFragmentDirections.roomMemberList().setRoomId(args.roomId));
        }
        binding.addPeopleGroup.setOnClickListener { v ->
            findNavController().navigate(RoomSettingsFragmentDirections.inviteUsersToRoom().setRoomId(args.roomId));
        }
        binding.filesGroup.setOnClickListener { v ->
            args.roomId?.let {
                val filesIntent = Intent(this.activity, RoomfilesListActivity::class.java)
                filesIntent.putExtra(RoomfilesListActivity.ROOM_ID, it);
                startActivity(filesIntent)
            }
        }
    }

    override fun getFragment(): Fragment {
        return this;
    }

    companion object {
        const val ROOM_ID = "ROOM_ID";
    }
}
