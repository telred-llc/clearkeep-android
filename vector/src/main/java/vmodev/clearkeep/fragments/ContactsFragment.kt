package vmodev.clearkeep.fragments

import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import im.vector.R
import im.vector.activity.MXCActionBarActivity
import im.vector.databinding.FragmentContactsBinding
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.factories.viewmodels.interfaces.IContactFragmentViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IContactFragment
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User
import javax.inject.Inject
import javax.inject.Named


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val USER_ID = "USER_ID"
private const val GO_TO_ROOM_CODE = 12432;

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ContactsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ContactsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ContactsFragment : DataBindingDaggerFragment(), IContactFragment, IListRoomRecyclerViewAdapter.ICallbackToGetUsers {
    private var listener: OnFragmentInteractionListener? = null
    @Inject
    lateinit var viewModelFactory: IContactFragmentViewModelFactory;
    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM_CONTACT)
    lateinit var listRoomAdapter: IListRoomRecyclerViewAdapter;
    lateinit var binding: FragmentContactsBinding;
    private lateinit var userId: String;
    private var onGoingRoom = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it?.getString(USER_ID, "");
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false, dataBindingComponent);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dividerItemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL);
        listRoomAdapter.setDataBindingComponent(dataBindingComponent)
        listRoomAdapter.setCallbackToGetUsers(this, viewLifecycleOwner, userId);
        binding.recyclerViewListContact.addItemDecoration(dividerItemDecoration);
        binding.recyclerViewListContact.adapter = listRoomAdapter.getAdapter();
        listRoomAdapter.setOnItemClick { room, i ->
            if (!onGoingRoom) {
                onGoingRoom = true;
                onClickGoRoom(room.id);
            }
        }
        binding.rooms = viewModelFactory.getViewModel().getListRoomByType();
        viewModelFactory.getViewModel().getListRoomByType().observe(viewLifecycleOwner, Observer { t ->
            listRoomAdapter.getAdapter().submitList(t?.data)
        })
        binding.lifecycleOwner = viewLifecycleOwner;

        viewModelFactory.getViewModel().setListType(arrayOf(1, 129))
    }

    override fun getUsers(roomId: String): LiveData<Resource<List<User>>> {
        return viewModelFactory.getViewModel().getRoomUserJoinResult(roomId);
    }

    // TODO: Rename method, update argument and hook method into UI event
    private fun onClickGoRoom(roomId: String) {
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
         * @return A new instance of fragment ContactsFragmentFactory.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userId: String) =
                ContactsFragment().apply {
                    arguments = Bundle().apply {
                        putString(USER_ID, userId);
                    }
                }
    }
}
