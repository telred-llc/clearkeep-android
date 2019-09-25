package vmodev.clearkeep.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.orhanobut.dialogplus.DialogPlus
import im.vector.R
import im.vector.activity.MXCActionBarActivity
import im.vector.databinding.FragmentFavourites2Binding
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.activities.RoomSettingsActivity
import vmodev.clearkeep.adapters.BottomDialogFavouriteRoomLongClick
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IFavouritesFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFavouritesFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractFavouritesFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val USER_ID = "USER_ID";
private const val GO_TO_ROOM_CODE = 12432;

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Favourites.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Favourites.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FavouritesFragment : DataBindingDaggerFragment(), IFragment, IListRoomRecyclerViewAdapter.ICallbackToGetUsers {
    // TODO: Rename and change types of parameters
    // You can declare variable to pass from activity is here

    private var listener: OnFragmentInteractionListener? = null

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractFavouritesFragmentViewModel>;
    @Inject
    lateinit var appExecutors: AppExecutors;
    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM)
    lateinit var listGroupRecyclerViewAdapter: IListRoomRecyclerViewAdapter;
    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM)
    lateinit var listDirectRecyclerViewAdapter: IListRoomRecyclerViewAdapter;

    lateinit var binding: FragmentFavourites2Binding;
    private lateinit var userId: String;
    private var onGoingRoom = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Deserialize is here
            userId = it.getString(USER_ID, "");
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites2, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner;
        listDirectRecyclerViewAdapter.setCallbackToGetUsers(this, viewLifecycleOwner, userId);
        listGroupRecyclerViewAdapter.setCallbackToGetUsers(this, viewLifecycleOwner, userId);
//        listGroupRecyclerViewAdapter.setDataBindingComponent(dataBindingComponent);
//        listDirectRecyclerViewAdapter.setDataBindingComponent(dataBindingComponent);
        listGroupRecyclerViewAdapter.setOnItemClick { room, i ->
            if (!onGoingRoom) {
                onGoingRoom = true;
//                gotoRoom(room.id);
            }
        }
        listDirectRecyclerViewAdapter.setOnItemClick { room, i ->
            if (!onGoingRoom) {
                onGoingRoom = true;
//                gotoRoom(room.id);
            }
        }
        listGroupRecyclerViewAdapter.setOnItemLongClick { room ->
            val bottomDialog = DialogPlus.newDialog(this.context)
                    .setAdapter(BottomDialogFavouriteRoomLongClick())
                    .setOnItemClickListener { dialog, item, view, position ->
//                        when (position) {
//                            1 -> removeFromFavourites(room.id);
//                            3 -> declineInvite(room.id);
//                            2 -> gotoRoomSettings(room.id);
//                            0 -> changeNotificationState(room.id, room.notificationState);
//                        }
                        dialog?.dismiss();
                    }.setContentBackgroundResource(R.drawable.background_radius_change_with_theme).create();
            bottomDialog.show();
        }
        listDirectRecyclerViewAdapter.setOnItemLongClick {
            val bottomDialog = DialogPlus.newDialog(this.context)
                    .setAdapter(BottomDialogFavouriteRoomLongClick())
                    .setOnItemClickListener { dialog, item, view, position ->
//                        when (position) {
//                            1 -> removeFromFavourites(it.id);
//                            3 -> declineInvite(it.id);
//                            2 -> gotoRoomSettings(it.id);
//                            0 -> changeNotificationState(it.id, it.notificationState);
//                        }
                        dialog?.dismiss();
                    }.setContentBackgroundResource(R.drawable.background_radius_change_with_theme).create();
            bottomDialog.show();
        }
        binding.roomsDirectMessage = viewModelFactory.getViewModel().getListTypeFavouritesDirectResult();
        binding.roomsGroupMessage = viewModelFactory.getViewModel().getListTypeFavouritesGroupResult();
        binding.recyclerViewListDirectChat.isNestedScrollingEnabled = false;
        binding.recyclerViewListGroupChat.isNestedScrollingEnabled = false;
        binding.recyclerViewListDirectChat.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL));
        binding.recyclerViewListGroupChat.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL));
        binding.recyclerViewListGroupChat.adapter = listGroupRecyclerViewAdapter.getAdapter();
        binding.recyclerViewListDirectChat.adapter = listDirectRecyclerViewAdapter.getAdapter();
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
        viewModelFactory.getViewModel().getListRoomListUserResult().observe(viewLifecycleOwner, Observer { t ->
            listDirectRecyclerViewAdapter.getAdapter().submitList(t?.data);
        });
        viewModelFactory.getViewModel().getListRoomListUserResult().observe(viewLifecycleOwner, Observer {
            listGroupRecyclerViewAdapter.getAdapter().submitList(it?.data);
        })
        viewModelFactory.getViewModel().setListTypeFavouritesDirect(arrayOf(129))
        viewModelFactory.getViewModel().setListTypeFavouritesGroup(arrayOf(130))
    }

    override fun getUsers(userIds: Array<String>): LiveData<Resource<List<User>>> {
        return viewModelFactory.getViewModel().getRoomUserJoinResult(userIds);
    }

    private fun gotoRoom(roomId: String) {
        val intentRoom = Intent(this.context, RoomActivity::class.java);
        intentRoom.putExtra(MXCActionBarActivity.EXTRA_MATRIX_ID, userId);
        intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, roomId);
        startActivityForResult(intentRoom, GO_TO_ROOM_CODE);
        onGoingRoom = false;
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

    private fun declineInvite(roomId: String) {
        binding.leaveRoom = viewModelFactory.getViewModel().getLeaveRoom();
        viewModelFactory.getViewModel().setLeaveRoom(roomId);
    }

    private fun gotoRoomSettings(roomId: String) {
        val intent = Intent(this.activity, RoomSettingsActivity::class.java);
        intent.putExtra(RoomSettingsActivity.ROOM_ID, roomId);
        startActivity(intent);
    }

    private fun removeFromFavourites(roomId: String) {
        binding.room = viewModelFactory.getViewModel().getRemoveFromFavouriteResult();
        viewModelFactory.getViewModel().setRemoveFromFavourite(roomId);
    }

    private fun changeNotificationState(roomId: String, state: Byte) {
        binding.room = viewModelFactory.getViewModel().getChangeNotificationStateResult();
        when (state) {
            0x01.toByte(), 0x02.toByte() -> viewModelFactory.getViewModel().setChangeNotificationState(roomId, 0x04);
            0x04.toByte() -> viewModelFactory.getViewModel().setChangeNotificationState(roomId, 0x02);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event

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
         * @param userId
         * @return A new instance of fragment Favourites.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userId: String) =
                FavouritesFragment().apply {
                    arguments = Bundle().apply {
                        // Put data is here
                        putString(USER_ID, userId);
                    }
                }
    }
}
