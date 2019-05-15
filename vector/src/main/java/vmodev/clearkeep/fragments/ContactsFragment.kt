package vmodev.clearkeep.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import im.vector.R
import im.vector.databinding.FragmentContactsBinding
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room
import vmodev.clearkeep.adapters.DirectMessageRecyclerViewAdapter
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.Interfaces.IListRoomOnFragmentInteractionListener
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import javax.inject.Inject
import javax.inject.Named


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val LIST_CONTACT = "LIST_CONTACT"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ContactsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ContactsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ContactsFragment : DataBindingDaggerFragment(), IFragment {
    private var listener: OnFragmentInteractionListener? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;
    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM_CONTACT)
    lateinit var listRoomAdapter: IListRoomRecyclerViewAdapter;

    lateinit var binding: FragmentContactsBinding;
    lateinit var roomViewModel: AbstractRoomViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
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
        roomViewModel = ViewModelProviders.of(this, viewModelFactory).get(AbstractRoomViewModel::class.java);
        val dividerItemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL);
        listRoomAdapter.setdataBindingComponent(dataBindingComponent)
        binding.recyclerViewListContact.addItemDecoration(dividerItemDecoration);
        binding.recyclerViewListContact.adapter = listRoomAdapter.getAdapter();
        listRoomAdapter.setOnItemClick { room, i ->
            onClickGoRoom(room.id);
        }
        binding.rooms = roomViewModel.getRoomsData();
        roomViewModel.getRoomsData().observe(viewLifecycleOwner, Observer { t ->
            listRoomAdapter.getAdapter().submitList(t?.data)
        })
        binding.lifecycleOwner = viewLifecycleOwner;

        roomViewModel.setFilter(arrayOf(1, 129))
    }

    // TODO: Rename method, update argument and hook method into UI event
    private fun onClickGoRoom(roomId: String) {
        listener?.onClickGoRoom(roomId);
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
         * @return A new instance of fragment ContactsFragmentFactory.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                ContactsFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
