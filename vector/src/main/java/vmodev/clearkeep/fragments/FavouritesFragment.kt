package vmodev.clearkeep.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.OnItemClickListener
import dagger.android.support.DaggerFragment
import im.vector.R
import im.vector.databinding.FragmentDirectMessageBinding
import im.vector.databinding.FragmentFavourites2Binding
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room
import vmodev.clearkeep.activities.RoomSettingsActivity
import vmodev.clearkeep.adapters.BottomDialogFavouriteRoomLongClick
import vmodev.clearkeep.adapters.BottomDialogRoomLongClick
import vmodev.clearkeep.adapters.DirectMessageRecyclerViewAdapter
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomRecyclerViewAdapter
import vmodev.clearkeep.binding.FragmentDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import javax.inject.Inject
import javax.inject.Named

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val LIST_FAVOURITES = "LIST_FAVOURITES"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Favourites.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Favourites.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FavouritesFragment : DataBindingDaggerFragment(), IFragment {
    // TODO: Rename and change types of parameters
    // You can declare variable to pass from activity is here

    private var listener: OnFragmentInteractionListener? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;
    @Inject
    lateinit var appExecutors: AppExecutors;
    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM)
    lateinit var listRoomRecyclerViewAdapter: IListRoomRecyclerViewAdapter;

    lateinit var binding: FragmentFavourites2Binding;

    lateinit var roomViewModel: AbstractRoomViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Deserialize is here
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites2, container, false, dataBindingComponent);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomViewModel = ViewModelProviders.of(this, viewModelFactory).get(AbstractRoomViewModel::class.java);
        binding.lifecycleOwner = viewLifecycleOwner;
        listRoomRecyclerViewAdapter.setdataBindingComponent(dataBindingComponent);
        listRoomRecyclerViewAdapter.setOnItemClick { room, i ->
            onClickGoRoom(room.id);
        }
        listRoomRecyclerViewAdapter.setOnItemLongClick { room ->
            val bottomDialog = DialogPlus.newDialog(this.context)
                    .setAdapter(BottomDialogFavouriteRoomLongClick())
                    .setOnItemClickListener { dialog, item, view, position ->
                        when (position) {
                            1 -> onClickRemoveFromFavourite(room.id);
                            3 -> onClickLeaveRoom(room.id);
                            2 -> onClickRoomSettings(room.id);
                        }
                        dialog?.dismiss();
                    }.create();
            bottomDialog.show();
        }
        binding.rooms = roomViewModel.getRoomsData();
        binding.recyclerViewListConversation.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding.recyclerViewListConversation.adapter = listRoomRecyclerViewAdapter.getAdapter();
        roomViewModel.getRoomsData().observe(viewLifecycleOwner, Observer { t ->
            listRoomRecyclerViewAdapter.getAdapter().submitList(t?.data);
        });
        roomViewModel!!.setFilter(arrayOf(129, 130))
    }

    // TODO: Rename method, update argument and hook method into UI event
    private fun onClickRoomSettings(id: String) {
        val intent = Intent(this.activity, RoomSettingsActivity::class.java);
        intent.putExtra(RoomSettingsActivity.ROOM_ID, id);
        startActivity(intent);
    }

    private fun onClickGoRoom(roomId: String) {
        listener?.onClickGoRoom(roomId);
    }

    private fun onClickLeaveRoom(roomId: String) {
        binding.room = roomViewModel.getLeaveRoom();
        roomViewModel.setLeaveRoom(roomId);
    }

    private fun onClickRemoveFromFavourite(roomId: String) {
        binding.roomObject = roomViewModel.getRemoveFromFavouriteResult();
        roomViewModel.setRemoveFromFavourite(roomId);
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
        fun onClickGoRoom(roomId: String);
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Favourites.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                FavouritesFragment().apply {
                    arguments = Bundle().apply {
                        // Put data is here
                    }
                }
    }
}
