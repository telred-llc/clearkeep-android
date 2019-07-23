package vmodev.clearkeep.fragments

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.dialogplus.DialogPlus
import im.vector.Matrix

import im.vector.R
import im.vector.activity.MXCActionBarActivity
import im.vector.databinding.FragmentListRoomBinding
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.activities.*
import vmodev.clearkeep.adapters.BottomDialogRoomLongClick
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.factories.viewmodels.interfaces.IListRoomFragmentViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IListRoomFragment
import javax.inject.Inject
import javax.inject.Named

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val USER_ID = "USER_ID"
private const val GO_TO_ROOM_CODE = 12432;

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ListRoomFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ListRoomFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ListRoomFragment : DataBindingDaggerFragment(), IListRoomFragment {

    @Inject
    lateinit var viewModelFactory: IListRoomFragmentViewModelFactory;
    @Inject
    lateinit var applcation: IApplication;
    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM)
    lateinit var listGroupRoomAdapter: IListRoomRecyclerViewAdapter;
    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM)
    lateinit var listDirectRoomAdapter: IListRoomRecyclerViewAdapter;

    // TODO: Rename and change types of parameters
    private var userId: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var binding: FragmentListRoomBinding;
    private var onGoingRoom = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_room, container, false, dataBindingComponent);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listDirectRoomAdapter.setdataBindingComponent(dataBindingComponent);
        listGroupRoomAdapter.setdataBindingComponent(dataBindingComponent);
        listDirectRoomAdapter.setOnItemClick { room, i ->
            when (i) {
                3 -> {
                    if (!onGoingRoom) {
                        onGoingRoom = true;
                        gotoRoom(room.id);
                    }
                }
                0 -> {
                    previewRoom(room.id);
                }
                1 -> {
                    joinRoom(room.id);
                }
                2 -> {
                    declideInvite(room.id);
                }
            }
        }
        listDirectRoomAdapter.setOnItemLongClick { room ->
            val bottomDialog = DialogPlus.newDialog(this.context)
                    .setAdapter(BottomDialogRoomLongClick(room.notificationState))
                    .setOnItemClickListener { dialog, item, view, position ->
                        when (position) {
                            3 -> {
                                declideInvite(room.id);
                            }
                            1 -> {
                                binding.room = viewModelFactory.getViewModel().getAddToFavouriteResult();
                                viewModelFactory.getViewModel().setAddToFavouriteRoomId(room.id);
                            }
                            2 -> {
                                val intent = Intent(this.activity, RoomSettingsActivity::class.java);
                                intent.putExtra(RoomSettingsActivity.ROOM_ID, room.id);
                                startActivity(intent);
                            }
                            4 -> {
                                val intentGoRoom = Intent(activity, MessageListActivity::class.java);
                                intentGoRoom.putExtra(MessageListActivity.ROOM_ID, room.id);
                                startActivity(intentGoRoom);
                            }
                            0 -> {
                                changeNotificationState(room.id, room.notificationState);
                            }
                        }

                        dialog?.dismiss();
                    }.setContentBackgroundResource(R.drawable.background_radius_change_with_theme).create();
            bottomDialog.show();
        }
        listGroupRoomAdapter.setOnItemLongClick { room ->
            val bottomDialog = DialogPlus.newDialog(this.context)
                    .setAdapter(BottomDialogRoomLongClick(room.notificationState))
                    .setOnItemClickListener { dialog, item, view, position ->
                        when (position) {
                            3 -> {
                                declideInvite(room.id);
                            }
                            1 -> {
                                binding.room = viewModelFactory.getViewModel().getAddToFavouriteResult();
                                viewModelFactory.getViewModel().setAddToFavouriteRoomId(room.id);
                            }
                            2 -> {
                                val intent = Intent(this.activity, RoomSettingsActivity::class.java);
                                intent.putExtra(RoomSettingsActivity.ROOM_ID, room.id);
                                startActivity(intent);
                            }
                            4 -> {
                                val intentGoRoom = Intent(activity, MessageListActivity::class.java);
                                intentGoRoom.putExtra(MessageListActivity.ROOM_ID, room.id);
                                startActivity(intentGoRoom);
                            }
                            0 -> {
                                changeNotificationState(room.id, room.notificationState);
                            }
                        }

                        dialog?.dismiss();
                    }.setContentBackgroundResource(R.drawable.background_radius_change_with_theme).create();
            bottomDialog.show();
        }
        listGroupRoomAdapter.setOnItemClick { room, i ->
            when (i) {
                3 -> {
                    if (!onGoingRoom) {
                        onGoingRoom = true;
                        gotoRoom(room.id);
                    }
                }
                0 -> {
                    previewRoom(room.id);
                }
                1 -> {
                    joinRoom(room.id);
                }
                2 -> {
                    declideInvite(room.id);
                }
            }
        }

        binding.recyclerViewListDirectChat.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding.recyclerViewListGroupChat.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding.recyclerViewListDirectChat.isNestedScrollingEnabled = false;
        binding.recyclerViewListGroupChat.isNestedScrollingEnabled = false;
        binding.recyclerViewListDirectChat.adapter = listDirectRoomAdapter.getAdapter();
        binding.recyclerViewListGroupChat.adapter = listGroupRoomAdapter.getAdapter();

        binding.listDirect = viewModelFactory.getViewModel().getListDirectRoomResult();
        binding.listGroup = viewModelFactory.getViewModel().getListGroupRoomResult();

        viewModelFactory.getViewModel().getListDirectRoomResult().observe(this.viewLifecycleOwner, Observer {
            listDirectRoomAdapter.getAdapter().submitList(it?.data);
        });
        viewModelFactory.getViewModel().getListGroupRoomResult().observe(this.viewLifecycleOwner, Observer {
            listGroupRoomAdapter.getAdapter().submitList(it?.data)
        });
        viewModelFactory.getViewModel().joinRoomWithIdResult().observe(this.viewLifecycleOwner, Observer {
            it?.data?.let { gotoRoom(it.id) }
        });
        binding.buttonStartDirectChat.setOnClickListener {
            val intentNewChat = Intent(context, FindAndCreateNewConversationActivity::class.java);
            startActivity(intentNewChat);
        }
        binding.buttonStartGroupChat.setOnClickListener {
            val intentNewChat = Intent(context, CreateNewRoomActivity::class.java);
            startActivity(intentNewChat);
        }
        binding.imageViewCreateNewRoom.setOnClickListener {
            val intentNewChat = Intent(context, CreateNewRoomActivity::class.java);
            startActivity(intentNewChat);
        }
        binding.imageViewCreateNewDirect.setOnClickListener {
            val intentNewChat = Intent(context, FindAndCreateNewConversationActivity::class.java);
            startActivity(intentNewChat);
        }
        binding.linearLayoutGroup.setOnClickListener {
            binding.expandableLayoutListGroup.isExpanded = !binding.expandableLayoutListGroup.isExpanded;
            if (binding.expandableLayoutListGroup.isExpanded) {
                binding.imageViewDirectionGroup.rotation = 0f;
            } else {
                binding.imageViewDirectionGroup.rotation = 270f;
            }
        }
        binding.linearLayoutDirect.setOnClickListener {
            binding.expandableLayoutListDirect.isExpanded = !binding.expandableLayoutListDirect.isExpanded;
            if (binding.expandableLayoutListDirect.isExpanded) {
                binding.imageViewDirectionDirect.rotation = 0f;
            } else {
                binding.imageViewDirectionDirect.rotation = 270f;
            }
        }
        binding.lifecycleOwner = this.viewLifecycleOwner;

        viewModelFactory.getViewModel().setFiltersDirectRoom(arrayOf(1, 65));
        viewModelFactory.getViewModel().setFiltersGroupRoom(arrayOf(2, 66));
    }

    private fun previewRoom(roomId: String) {
        val intent = Intent(this.context, PreviewInviteRoomActivity::class.java);
        intent.putExtra("ROOM_ID", roomId);
        startActivity(intent);
    }

    private fun joinRoom(roomId: String) {
        binding.room = viewModelFactory.getViewModel().joinRoomWithIdResult();
        viewModelFactory.getViewModel().setRoomIdForJoinRoom(roomId);
    }

    private fun gotoRoom(roomId: String) {
        val intentRoom = Intent(this.context, RoomActivity::class.java);
        intentRoom.putExtra(MXCActionBarActivity.EXTRA_MATRIX_ID, userId);
        intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, roomId);
        startActivityForResult(intentRoom, GO_TO_ROOM_CODE);
        onGoingRoom = false;
    }

    private fun changeNotificationState(roomId: String, state: Byte) {
        binding.room = viewModelFactory.getViewModel().getChangeNotificationStateResult();
        when (state) {
            0x01.toByte(), 0x02.toByte() -> viewModelFactory.getViewModel().setChangeNotificationState(roomId, 0x04);
            0x04.toByte() -> viewModelFactory.getViewModel().setChangeNotificationState(roomId, 0x02);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GO_TO_ROOM_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                binding.room = viewModelFactory.getViewModel().getUpdateRoomNotifyResult();
                viewModelFactory.getViewModel().setIdForUpdateRoomNotify(it.getStringExtra(RoomActivity.RESULT_ROOM_ID))
            }
        }
    }

    private fun declideInvite(roomId: String) {
        binding.leaveRoom = viewModelFactory.getViewModel().getLeaveRoomWithIdResult();
        viewModelFactory.getViewModel().setLeaveRoomId(roomId);
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun getFragment(): Fragment {
        return this;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListRoomFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userId: String) =
                ListRoomFragment().apply {
                    arguments = Bundle().apply {
                        putString(USER_ID, userId)
                    }
                }
    }
}
