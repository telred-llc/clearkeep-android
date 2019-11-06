package vmodev.clearkeep.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.dialogplus.DialogPlus
import im.vector.R
import im.vector.activity.MXCActionBarActivity
import im.vector.databinding.FragmentListRoomBinding
import vmodev.clearkeep.activities.*
import vmodev.clearkeep.adapters.BottomDialogFavouriteRoomLongClick
import vmodev.clearkeep.adapters.BottomDialogRoomLongClick
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.Interfaces.IListRoomWithStickyHeaderRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomWithStickyHeaderRecyclerViewAdapter
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.customviews.StickyHeaderItemDecoration
import vmodev.clearkeep.customviews.interfaces.IStickyHeaderItemDecorationListener
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.*
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
class ListRoomFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractListRoomFragmentViewModel>;
    @Inject
    lateinit var applcation: IApplication;
    //    @Inject
//    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM)
//    lateinit var listGroupRoomAdapter: IListRoomRecyclerViewAdapter;
//    @Inject
//    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM)
//    lateinit var listDirectRoomAdapter: IListRoomRecyclerViewAdapter;
    @Inject
    lateinit var listFavouritesRoomAdapter: IListRoomWithStickyHeaderRecyclerViewAdapter<ListRoomWithStickyHeaderRecyclerViewAdapter.StickyHeaderData>;

    private lateinit var binding: FragmentListRoomBinding;
    private var onGoingRoom = false;
    //    private var roomList: Int? = 0
//    private var directList: Int? = 0
    private var currentRoomId: String = ""
    //    private var alertDialog: AlertDialog? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
    private val rooms = ArrayList<RoomListUser>();
    private val favouritesCurrentList = ArrayList<RoomListUser>();
    private val directsCurrentList = ArrayList<RoomListUser>();
    private val roomsCurrentList = ArrayList<RoomListUser>();

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

    private var listExplanable: List<RoomUserList> = ArrayList();
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
                        .setAdapter(BottomDialogFavouriteRoomLongClick(it.notificationState, it.type))
                        .setOnItemClickListener { dialog, item, view, position ->
                            when (position) {
                                3 -> {
                                    declineInvite(it.id);
                                }
                                1 -> {
                                    if (it.type == 0x01 or 128 || it.type == 0x02 or 128) {
                                        binding.room = viewModelFactory.getViewModel().getRemoveFromFavouriteResult();
                                        viewModelFactory.getViewModel().setRemoveFromFavourite(it.id);
                                    } else {
                                        binding.room = viewModelFactory.getViewModel().getAddToFavouriteResult()
                                        viewModelFactory.getViewModel().setAddToFavouriteRoomId(it.id);
                                    }
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
        listFavouritesRoomAdapter.setOnItemStickyHeaderClick {
            Log.d("Position", it.toString());
            if (!expandable) {
                rooms.removeAll(favouritesCurrentList);
                rooms.add(0, RoomListUser(null, null, null))
                listFavouritesRoomAdapter.getAdapter().submitList(rooms);
                listFavouritesRoomAdapter.getAdapter().notifyDataSetChanged();
                expandable = true;
            } else {
                rooms.removeAt(0);
                rooms.addAll(0, favouritesCurrentList);
                listFavouritesRoomAdapter.getAdapter().submitList(rooms);
                listFavouritesRoomAdapter.getAdapter().notifyDataSetChanged();
                expandable = false;
            }
        }

        binding.recyclerViewListFavouritesChat.addItemDecoration(StickyHeaderItemDecoration(listFavouritesRoomAdapter as IStickyHeaderItemDecorationListener, binding.recyclerViewListFavouritesChat))
        binding.recyclerViewListFavouritesChat.addItemDecoration(DividerItemDecoration(this.activity, DividerItemDecoration.VERTICAL));
        binding.recyclerViewListFavouritesChat.isNestedScrollingEnabled = false;
        binding.recyclerViewListFavouritesChat.adapter = listFavouritesRoomAdapter.getAdapter();
        binding.listFavourites = viewModelFactory.getViewModel().getListDirectRoomResult();
        viewModelFactory.getViewModel().getListFavouritesResult().observe(this.viewLifecycleOwner, Observer {
            it?.data?.let {
                rooms.removeAll(favouritesCurrentList);
                favouritesCurrentList.clear();
                favouritesCurrentList.add(RoomListUser(null, null, null))
                favouritesCurrentList.addAll(it);
                rooms.addAll(0, favouritesCurrentList);
                listFavouritesRoomAdapter.getAdapter().submitList(rooms);
                listFavouritesRoomAdapter.setListHeader(arrayListOf
                (ListRoomWithStickyHeaderRecyclerViewAdapter.StickyHeaderData(0, R.drawable.ic_star_24dp, getString(R.string.favourites), favouritesCurrentList.size),
                        ListRoomWithStickyHeaderRecyclerViewAdapter.StickyHeaderData(favouritesCurrentList.size, R.drawable.ic_star_24dp, getString(R.string.room_chats), roomsCurrentList.size)
                        , ListRoomWithStickyHeaderRecyclerViewAdapter.StickyHeaderData(favouritesCurrentList.size + roomsCurrentList.size, R.drawable.ic_star_24dp, getString(R.string.direct_chats), directsCurrentList.size)));
                listFavouritesRoomAdapter.getAdapter().notifyDataSetChanged();
            }
        });
        viewModelFactory.getViewModel().getListDirectRoomResult().observe(this.viewLifecycleOwner, Observer {
            it?.data?.let {
                rooms.removeAll(directsCurrentList);
                directsCurrentList.clear();
                directsCurrentList.add(RoomListUser(null, null, null))
                directsCurrentList.addAll(it);
                rooms.addAll(favouritesCurrentList.size + roomsCurrentList.size, directsCurrentList)
                listFavouritesRoomAdapter.getAdapter().submitList(rooms);
                listFavouritesRoomAdapter.setListHeader(arrayListOf
                (ListRoomWithStickyHeaderRecyclerViewAdapter.StickyHeaderData(0, R.drawable.ic_star_24dp, getString(R.string.favourites), favouritesCurrentList.size),
                        ListRoomWithStickyHeaderRecyclerViewAdapter.StickyHeaderData(favouritesCurrentList.size, R.drawable.ic_star_24dp, getString(R.string.room_chats), roomsCurrentList.size)
                        , ListRoomWithStickyHeaderRecyclerViewAdapter.StickyHeaderData(favouritesCurrentList.size + roomsCurrentList.size, R.drawable.ic_star_24dp, getString(R.string.direct_chats), directsCurrentList.size)));
                listFavouritesRoomAdapter.getAdapter().notifyDataSetChanged();
            }
        });
        viewModelFactory.getViewModel().getListGroupRoomResult().observe(this.viewLifecycleOwner, Observer {
            it?.data?.let {
                rooms.removeAll(roomsCurrentList);
                roomsCurrentList.clear();
                roomsCurrentList.add(RoomListUser(null, null, null))
                roomsCurrentList.addAll(it);
                rooms.addAll(favouritesCurrentList.size, roomsCurrentList)
                listFavouritesRoomAdapter.getAdapter().submitList(rooms);
                listFavouritesRoomAdapter.setListHeader(arrayListOf
                (ListRoomWithStickyHeaderRecyclerViewAdapter.StickyHeaderData(0, R.drawable.ic_star_24dp, getString(R.string.favourites), favouritesCurrentList.size),
                        ListRoomWithStickyHeaderRecyclerViewAdapter.StickyHeaderData(favouritesCurrentList.size, R.drawable.ic_star_24dp, getString(R.string.room_chats), roomsCurrentList.size)
                        , ListRoomWithStickyHeaderRecyclerViewAdapter.StickyHeaderData(favouritesCurrentList.size + roomsCurrentList.size, R.drawable.ic_star_24dp, getString(R.string.direct_chats), directsCurrentList.size)));
                listFavouritesRoomAdapter.getAdapter().notifyDataSetChanged();
            }
        });
//        binding.linearLayoutFavourites.setOnClickListener {
//            binding.expandableLayoutListFavourites.isExpanded = !binding.expandableLayoutListFavourites.isExpanded;
//            if (binding.expandableLayoutListFavourites.isExpanded) {
//                binding.imageViewDirectionFavourites.rotation = 0f;
//            } else {
//                binding.imageViewDirectionFavourites.rotation = 270f;
//            }
//        }
    }

    private var expandable: Boolean = false;

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

//        val intentRoom = Intent(this.context, RoomDetailActivity::class.java);
//        intentRoom.putExtra(RoomDetailActivity.ROOM_ID, roomId);
//        startActivity(intentRoom);

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
