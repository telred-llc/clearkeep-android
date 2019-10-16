package vmodev.clearkeep.fragments

import RecyclerSectionItemDecoration
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import im.vector.R
import im.vector.activity.MXCActionBarActivity
import im.vector.databinding.FragmentContactsBinding
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.Interfaces.SectionCallback
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.factories.viewmodels.interfaces.IContactFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IContactFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractContactFragmentViewModel
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
class ContactsFragment : DataBindingDaggerFragment(), IFragment, IListRoomRecyclerViewAdapter.ICallbackToGetUsers {
    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractContactFragmentViewModel>;
    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.ROOM)
    lateinit var listRoomAdapter: IListRoomRecyclerViewAdapter;
    @Inject
    lateinit var application: IApplication;
    lateinit var binding: FragmentContactsBinding;
    private var onGoingRoom = false;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dividerItemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL);
        listRoomAdapter.setCallbackToGetUsers(this, viewLifecycleOwner, application.getUserId());
        binding.recyclerViewListContact.addItemDecoration(dividerItemDecoration);
        binding.recyclerViewListContact.adapter = listRoomAdapter.getAdapter();
        listRoomAdapter.setOnItemClick { room, i ->
            room.room?.let {
                if (!onGoingRoom) {
                    onGoingRoom = true;
                    onClickGoRoom(it.id); }
            }
        }
        listRoomAdapter.setOnItemLongClick { }
        listRoomAdapter.setCallbackToGetUsers(this, viewLifecycleOwner, application.getUserId());
        binding.rooms = viewModelFactory.getViewModel().getListRoomByType();
        viewModelFactory.getViewModel().getListRoomByType().observe(viewLifecycleOwner, Observer { t ->

                listRoomAdapter.getAdapter().submitList(t?.data)
                val sectionItemDecoration = t.data?.let { getListRoomName(it) }?.let { getSectionCallback(it) }?.let {
                    RecyclerSectionItemDecoration(resources.getDimensionPixelSize(R.dimen.header),
                            true,
                            it)
                }
            if (sectionItemDecoration != null) {
                binding.recyclerViewListContact.addItemDecoration(sectionItemDecoration)
            }

        })
        binding.lifecycleOwner = viewLifecycleOwner;

        viewModelFactory.getViewModel().setListType(arrayOf(1, 129))
    }

    override fun getUsers(userIds: Array<String>): LiveData<Resource<List<User>>> {
        return viewModelFactory.getViewModel().getRoomUserJoinResult(userIds);
    }

    // TODO: Rename method, update argument and hook method into UI event
    private fun onClickGoRoom(roomId: String) {
        val intentRoom = Intent(this.context, RoomActivity::class.java);
        intentRoom.putExtra(MXCActionBarActivity.EXTRA_MATRIX_ID, application.getUserId());
        intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, roomId);
        startActivityForResult(intentRoom, GO_TO_ROOM_CODE);
        onGoingRoom = false;
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

    override fun getFragment(): Fragment {
        return this;
    }


    fun getListRoomName(data: List<RoomListUser>): List<String> {
        val dataHeader = ArrayList<String>()
            for (item in data) {
                item.room?.name?.let { dataHeader.add(it.toUpperCase()) }
        }
        return dataHeader
    }

    private fun getSectionCallback(people: List<String>): SectionCallback {
        return object : SectionCallback {
            override fun isSection(position: Int): Boolean {

                return position == 0 || people[position][0] != people[position - 1][0]
            }

            override fun getSectionHeader(position: Int): CharSequence {
                return people[position]
                        .subSequence(0,
                                1)
            }
        }
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
