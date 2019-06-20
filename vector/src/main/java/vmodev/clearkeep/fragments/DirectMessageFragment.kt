package vmodev.clearkeep.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.dialogplus.DialogPlus
import im.vector.R
import im.vector.databinding.FragmentDirectMessageBinding
import vmodev.clearkeep.activities.MessageListActivity
import vmodev.clearkeep.activities.FindAndCreateNewConversationActivity
import vmodev.clearkeep.activities.RoomSettingsActivity
import vmodev.clearkeep.adapters.BottomDialogRoomLongClick
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IDirectMessageFragmentViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IDriectMessageFragment
import vmodev.clearkeep.fragments.Interfaces.IListRoomOnFragmentInteractionListener
import javax.inject.Inject
import javax.inject.Named

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DirectMessageFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DirectMessageFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DirectMessageFragment : DataBindingDaggerFragment(), IDriectMessageFragment {
    private var listener: IListRoomOnFragmentInteractionListener? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;
    @Inject
    lateinit var appExecutors: AppExecutors;
    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM)
    lateinit var listRoomAdapter: IListRoomRecyclerViewAdapter;

    lateinit var binding: FragmentDirectMessageBinding;

    @Inject
    lateinit var directMessageViewModelFactory: IDirectMessageFragmentViewModelFactory;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_direct_message, container, false, dataBindingComponent);
        return binding!!.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        roomViewModel = ViewModelProviders.of(this, viewModelFactory).get(AbstractRoomViewModel::class.java);
        binding.lifecycleOwner = viewLifecycleOwner;
        listRoomAdapter.setdataBindingComponent(dataBindingComponent);
        listRoomAdapter.setOnItemClick { room, i ->
            when (i) {
                3 -> onClickGoRoom(room.id);
                0 -> onClickItemPreview(room.id);
                1 -> onClickJoinRoom(room.id);
                2 -> onClickItemDecline(room.id);
            }
        }
        listRoomAdapter.setOnItemLongClick { room ->
            val bottomDialog = DialogPlus.newDialog(this.context)
                    .setAdapter(BottomDialogRoomLongClick())
                    .setOnItemClickListener { dialog, item, view, position ->
                        when (position) {
                            3 -> onClickItemDecline(room.id);
                            1 -> onClickAddToFavourite(room.id);
                            2 -> onClickRoomSettings(room.id);
                            4 -> onClickGoRoomPreview(room.id);
                        }

                        dialog?.dismiss();
                    }.setContentBackgroundResource(R.drawable.background_radius_change_with_theme).create();
            bottomDialog.show();
        }
        binding.rooms = directMessageViewModelFactory.getViewModel().getListRoomByType();
        binding.buttonStartDirectChat.setOnClickListener {
            val intentNewChat = Intent(context, FindAndCreateNewConversationActivity::class.java);
            startActivity(intentNewChat);
        }
        binding.recyclerViewListConversation.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding.recyclerViewListConversation.adapter = listRoomAdapter.getAdapter();
        directMessageViewModelFactory.getViewModel().getListRoomByType().observe(viewLifecycleOwner, Observer { t ->
            listRoomAdapter!!.getAdapter().submitList(t?.data);
        });
        directMessageViewModelFactory.getViewModel().setListType(arrayOf(1, 65))
    }

    private fun onClickGoRoomPreview(id: String) {
        val intentGoRoom = Intent(activity, MessageListActivity::class.java);
        intentGoRoom.putExtra(MessageListActivity.ROOM_ID, id);
        startActivity(intentGoRoom);
    }

    private fun onClickRoomSettings(id: String) {
        val intent = Intent(this.activity, RoomSettingsActivity::class.java);
        intent.putExtra(RoomSettingsActivity.ROOM_ID, id);
        startActivity(intent);
    }

    private fun onClickJoinRoom(roomId: String) {
        listener?.onClickItemJoin(roomId);
    }

    private fun onClickAddToFavourite(roomId: String) {
        binding.roomObject = directMessageViewModelFactory.getViewModel().getAddToFavouriteResult();
        directMessageViewModelFactory.getViewModel().setAddToFavourite(roomId);
    }

    private fun onClickItemDecline(roomId: String) {
//        listener?.onClickItemDecline(roomId);
        binding.room = directMessageViewModelFactory.getViewModel().getLeaveRoom();
        directMessageViewModelFactory.getViewModel().setLeaveRoom(roomId);
    }

    private fun onClickItemPreview(roomId: String) {
        listener?.onClickItemPreview(roomId);
    }

    private fun onClickGoRoom(roomId: String) {
        listener?.onClickGoRoom(roomId);
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IListRoomOnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DirectMessageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                DirectMessageFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }

    override fun getFragment(): Fragment {
        return this;
    }
}
