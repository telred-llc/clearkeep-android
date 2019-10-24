package vmodev.clearkeep.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.orhanobut.dialogplus.DialogPlus
import im.vector.R
import im.vector.activity.MXCActionBarActivity
import im.vector.databinding.FragmentListRoomBinding
import vmodev.clearkeep.activities.*
import vmodev.clearkeep.adapters.BottomDialogFavouriteRoomLongClick
import vmodev.clearkeep.adapters.BottomDialogRoomLongClick
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomStickyHeaderRecyclerViewAdapter
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractListRoomFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

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
class ListRoomFragment : DataBindingDaggerFragment(), IFragment, IListRoomRecyclerViewAdapter.ICallbackToGetUsers {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractListRoomFragmentViewModel>;
    @Inject
    lateinit var applcation: IApplication;

    @Inject
    lateinit var appExecutors: AppExecutors;
    //    @Inject
//    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM)
//    lateinit var listGroupRoomAdapter: IListRoomRecyclerViewAdapter;
//    @Inject
//    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM)
//    lateinit var listDirectRoomAdapter: IListRoomRecyclerViewAdapter;
    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM)
    lateinit var listFavouritesRoomAdapter: IListRoomRecyclerViewAdapter;
    private lateinit var adapter:ListRoomStickyHeaderRecyclerViewAdapter
    private lateinit var binding: FragmentListRoomBinding;
    private var onGoingRoom = false;
    private var roomList: Int? = 0
    private var derectList: Int? = 0
    private var currentRoomId: String = ""
    private var alertDialog: AlertDialog? = null
    private var listRoom: HashMap<String, List<RoomListUser>>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment     
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_room, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initListDirectChat();
//        initListGroupChat();
        listRoom = HashMap()
        initListFavouriteChat();
        viewModelFactory.getViewModel().joinRoomWithIdResult().observe(this.viewLifecycleOwner, Observer {
            it?.data?.let {
                if (it.id != currentRoomId) {
                    currentRoomId = it.id
                    gotoRoom(currentRoomId)
                }
            }
        });
//        binding.buttonStartDirectChat.setOnClickListener {
//            val intentNewChat = Intent(context, NewRoomActivity::class.java);
//            intentNewChat.putExtra(NewRoomActivity.START_WITH, 0);
//            startActivity(intentNewChat);
//        }
//        binding.buttonStartGroupChat.setOnClickListener {
//            val intentNewChat = Intent(context, NewRoomActivity::class.java);
//            intentNewChat.putExtra(NewRoomActivity.START_WITH, 1);
//            startActivity(intentNewChat);
//        }
//        binding.imageViewCreateNewRoom.setOnClickListener {
//            val intentNewChat = Intent(context, NewRoomActivity::class.java);
//            intentNewChat.putExtra(NewRoomActivity.START_WITH, 1);
//            startActivity(intentNewChat);
//        }
//        binding.imageViewCreateNewDirect.setOnClickListener {
//            val intentNewChat = Intent(context, NewRoomActivity::class.java);
//            intentNewChat.putExtra(NewRoomActivity.START_WITH, 0);
//            startActivity(intentNewChat);
//        }

        binding.lifecycleOwner = viewLifecycleOwner

        viewModelFactory.getViewModel().setFiltersDirectRoom(arrayOf(1, 65));
        viewModelFactory.getViewModel().setFiltersGroupRoom(arrayOf(2, 66));
        viewModelFactory.getViewModel().setFiltersFavouriteRoom(arrayOf(129, 130))
    }

    private fun initListFavouriteChat() {
//        listFavouritesRoomAdapter.setDataBindingComponent(dataBindingComponent);
        listFavouritesRoomAdapter.setOnItemClick { room, i ->
            room.room?.let {
                when (i) {
                    3 -> {
                        if (!onGoingRoom) {
                            onGoingRoom = true;
                            gotoRoom(it.id);
                        }
                    }
                    0 -> {
                        previewRoom(it.id);
                    }
                    1 -> {
                        joinRoom(it.id);
                    }
                    2 -> {
                        declineInvite(it.id);
                    }
                }
            }
        }
        listFavouritesRoomAdapter.setOnItemLongClick { room ->
            room.room?.let {
                val bottomDialog = DialogPlus.newDialog(this.context)
                        .setAdapter(BottomDialogFavouriteRoomLongClick(it.notificationState))
                        .setOnItemClickListener { dialog, item, view, position ->
                            when (position) {
                                3 -> {
                                    declineInvite(it.id);
                                }
                                1 -> {
                                    binding.room = viewModelFactory.getViewModel().getRemoveFromFavouriteResult();
                                    viewModelFactory.getViewModel().setRemoveFromFavourite(it.id);
                                }
                                2 -> {
                                    val intent = Intent(this.activity, RoomSettingsActivity::class.java);
                                    intent.putExtra(RoomSettingsActivity.ROOM_ID, it.id);
                                    startActivity(intent);
                                }
                                4 -> {
                                    val intentGoRoom = Intent(activity, MessageListActivity::class.java);
                                    intentGoRoom.putExtra(MessageListActivity.ROOM_ID, it.id);
                                    startActivity(intentGoRoom);
                                }
                                0 -> {
                                    changeNotificationState(it.id, it.notificationState);
                                }
                            }

                            dialog?.dismiss();
                        }.setContentBackgroundResource(R.drawable.background_radius_change_with_theme).create();
                bottomDialog.show();
            }
        }
        binding.recyclerViewListFavouritesChat.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding.recyclerViewListFavouritesChat.isNestedScrollingEnabled = false;
        binding.recyclerViewListFavouritesChat.adapter = listFavouritesRoomAdapter.getAdapter();
        binding.listFavourites = viewModelFactory.getViewModel().getListFavouritesResult();
        viewModelFactory.getViewModel().getListFavouritesResult().observe(this.viewLifecycleOwner, Observer {
            //            listFavouritesRoomAdapter.getAdapter().submitList(it?.data);
            if (it?.status == Status.SUCCESS) {
                it.data?.let { it1 -> listRoom?.put("Favourties", it1) }
            }
        });

        viewModelFactory.getViewModel().getListGroupRoomResult().observe(this.viewLifecycleOwner, Observer {
            //                            listGroupRoomAdapter.getAdapter().submitList(it?.data)
            if (it?.status == Status.SUCCESS) {
                it.data?.let { it2 -> listRoom?.put("Direct Messages", it2) }
            }

        });

        viewModelFactory.getViewModel().getListDirectRoomResult().observe(this.viewLifecycleOwner, Observer {
            //            listDirectRoomAdapter.getAdapter().submitList(it?.data);
            if (it?.status == Status.SUCCESS) {
                it.data?.let { it3 -> listRoom?.put("Rooms", it3) }
            }

//        binding.linearLayoutFavourites.setOnClickListener {
//            binding.expandableLayoutListFavourites.isExpanded = !binding.expandableLayoutListFavourites.isExpanded;
//            if (binding.expandableLayoutListFavourites.isExpanded) {
//                binding.imageViewDirectionFavourites.rotation = 0f;
//            } else {
//                binding.imageViewDirectionFavourites.rotation = 270f;
//            }
            })
        }
//    listFavouritesRoomAdapter.getAdapter().submitList(li);

//    private fun initListDirectChat() {
//        listDirectRoomAdapter.setDataBindingComponent(dataBindingComponent);
//        listDirectRoomAdapter.setCallbackToGetUsers(this, viewLifecycleOwner, applcation.getUserId());

//        listDirectRoomAdapter.setOnItemClick { room, i ->
//            room.room?.let {
//                when (i) {
//                    3 -> {
//                        if (!onGoingRoom) {
//                            onGoingRoom = true;
//                            gotoRoom(it.id);
//                        }
//                    }
//                    0 -> {
//                        previewRoom(it.id);
//                    }
//                    1 -> {
//                        joinRoom(it.id);
//                    }
//                    2 -> {
//                        declineInvite(it.id);
//                    }
//                }
//            }
//        }
//        listDirectRoomAdapter.setOnItemLongClick { room ->
//            room.room?.let {
//                val bottomDialog = DialogPlus.newDialog(this.context)
//                        .setAdapter(BottomDialogRoomLongClick(it.notificationState))
//                        .setOnItemClickListener { dialog, item, view, position ->
//                            when (position) {
//                                3 -> {
//                                    declineInvite(it.id);
//                                }
//                                1 -> {
//                                    binding.room = viewModelFactory.getViewModel().getAddToFavouriteResult();
//                                    viewModelFactory.getViewModel().setAddToFavouriteRoomId(it.id);
//                                }
//                                2 -> {
//                                    val intent = Intent(this.activity, RoomSettingsActivity::class.java);
//                                    intent.putExtra(RoomSettingsActivity.ROOM_ID, it.id);
//                                    startActivity(intent);
//                                }
//                                4 -> {
//                                    val intentGoRoom = Intent(activity, MessageListActivity::class.java);
//                                    intentGoRoom.putExtra(MessageListActivity.ROOM_ID, it.id);
//                                    startActivity(intentGoRoom);
//                                }
//                                0 -> {
//                                    changeNotificationState(it.id, it.notificationState);
//                                }
//                            }
//
//                            dialog?.dismiss();
//                        }.setContentBackgroundResource(R.drawable.background_radius_change_with_theme).create();
//                bottomDialog.show();
//            }
//        }
//        binding.recyclerViewListDirectChat.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
//        binding.recyclerViewListDirectChat.isNestedScrollingEnabled = false;
//        binding.recyclerViewListDirectChat.adapter = listDirectRoomAdapter.getAdapter();
//        binding.listDirect = viewModelFactory.getViewModel().getListDirectRoomResult();
//        viewModelFactory.getViewModel().getListDirectRoomResult().observe(this.viewLifecycleOwner, Observer {
//            listDirectRoomAdapter.getAdapter().submitList(it?.data);
//            if (it?.status == Status.SUCCESS) {
//                derectList = it?.data!!.size
//            }

//        });
//        binding.linearLayoutDirect.setOnClickListener {
//            binding.expandableLayoutListDirect.isExpanded = !binding.expandableLayoutListDirect.isExpanded;
//            if (binding.expandableLayoutListDirect.isExpanded) {
//                binding.imageViewDirectionDirect.rotation = 0f;
//                if (derectList == 0) {
//                    binding.layoutEmptyDerect.visibility = View.VISIBLE
//                }
//
//            } else {
//                binding.imageViewDirectionDirect.rotation = 270f;
//                if (derectList == 0) {
//                    binding.layoutEmptyDerect.visibility = View.GONE
//                }
//
//            }
//        }
//    }

//private fun initListGroupChat() {
//        listGroupRoomAdapter.setDataBindingComponent(dataBindingComponent);
//        listGroupRoomAdapter.setCallbackToGetUsers(this, viewLifecycleOwner, applcation.getUserId());
//        listGroupRoomAdapter.setOnItemLongClick { room ->
//            room.room?.let {
//                val bottomDialog = DialogPlus.newDialog(this.context)
//                        .setAdapter(BottomDialogRoomLongClick(it.notificationState))
//                        .setOnItemClickListener { dialog, item, view, position ->
//                            when (position) {
//                                3 -> {
//                                    declineInvite(it.id);
//                                }
//                                1 -> {
//                                    binding.room = viewModelFactory.getViewModel().getAddToFavouriteResult();
//                                    viewModelFactory.getViewModel().setAddToFavouriteRoomId(it.id);
//                                }
//                                2 -> {
//                                    val intent = Intent(this.activity, RoomSettingsActivity::class.java);
//                                    intent.putExtra(RoomSettingsActivity.ROOM_ID, it.id);
//                                    startActivity(intent);
//                                }
//                                4 -> {
//                                    val intentGoRoom = Intent(activity, MessageListActivity::class.java);
//                                    intentGoRoom.putExtra(MessageListActivity.ROOM_ID, it.id);
//                                    startActivity(intentGoRoom);
//                                }
//                                0 -> {
//                                    changeNotificationState(it.id, it.notificationState);
//                                }
//                            }
//
//                            dialog?.dismiss();
//                        }.setContentBackgroundResource(R.drawable.background_radius_change_with_theme).create();
//                bottomDialog.show();
//            }
//        }
//        listGroupRoomAdapter.setOnItemClick { room, i ->
//            room.room?.let {
//                when (i) {
//                    3 -> {
//                        if (!onGoingRoom) {
//                            onGoingRoom = true;
//                            gotoRoom(it.id);
//                        }
//                    }
//                    0 -> {
//                        previewRoom(it.id);
//                    }
//                    1 -> {
//                        joinRoom(it.id);
//                    }
//                    2 -> {
//                        declineInvite(it.id);
//                    }
//                }
//            }
//        }
//        binding.recyclerViewListGroupChat.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
//        binding.recyclerViewListGroupChat.isNestedScrollingEnabled = false;
//        binding.recyclerViewListGroupChat.adapter = listGroupRoomAdapter.getAdapter();
//        binding.listGroup = viewModelFactory.getViewModel().getListGroupRoomResult();
//        viewModelFactory.getViewModel().getListGroupRoomResult().observe(this.viewLifecycleOwner, Observer {
//            listGroupRoomAdapter.getAdapter().submitList(it?.data)
//            if (it?.status == Status.SUCCESS) {
//                roomList = it?.data!!.size
//            }
//
//        });
//        binding.linearLayoutGroup.setOnClickListener {
//            binding.expandableLayoutListGroup.isExpanded = !binding.expandableLayoutListGroup.isExpanded;
//            if (binding.expandableLayoutListGroup.isExpanded) {
//                binding.imageViewDirectionGroup.rotation = 0f;
//                if (roomList == 0) {
//                    binding.layoutEmptyRoom.visibility = View.VISIBLE
//                }
//            } else {
//                binding.imageViewDirectionGroup.rotation = 270f;
//                if (roomList == 0) {
//                    binding.layoutEmptyRoom.visibility = View.GONE
//                }
//
//            }
//        }
//}
    override fun getUsers(userIds: Array<String>): LiveData<Resource<List<User>>> {
        return viewModelFactory.getViewModel().getRoomUserJoinResult(userIds);
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
        intentRoom.putExtra(MXCActionBarActivity.EXTRA_MATRIX_ID, applcation.getUserId());
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
        if (requestCode == GO_TO_ROOM_CODE && resultCode == -1) {
            data?.let {
                binding.room = viewModelFactory.getViewModel().getUpdateRoomNotifyResult();
                viewModelFactory.getViewModel().setIdForUpdateRoomNotify(it.getStringExtra(RoomActivity.RESULT_ROOM_ID))
            }
        }
    }

    private fun declineInvite(roomId: String) {
        AlertDialog.Builder(activity!!).setTitle(R.string.leave_room)
                .setMessage(R.string.do_you_want_leave_room)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes) { dialog, v ->
                    binding.leaveRoom = viewModelFactory.getViewModel().getLeaveRoomWithIdResult();
                    viewModelFactory.getViewModel().setLeaveRoomId(roomId);

                }.show();
    }

    override fun getFragment(): Fragment {
        return this;
    }
}
