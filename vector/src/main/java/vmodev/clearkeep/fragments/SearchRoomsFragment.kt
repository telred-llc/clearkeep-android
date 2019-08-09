package vmodev.clearkeep.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import im.vector.R
import im.vector.activity.MXCActionBarActivity
import im.vector.databinding.FragmentSearchRoomsBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.binding.FragmentDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.ISearchFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchRoomsFragmentViewModel
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val USER_ID = "USER_ID";

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SearchRoomsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SearchRoomsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SearchRoomsFragment : DataBindingDaggerFragment(), ISearchFragment {
    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var userId: String;

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractSearchRoomsFragmentViewModel>;
    @Inject
    lateinit var appExecutors: AppExecutors;
    @Inject
    lateinit var listRoomInviteRecyclerViewAdapter: IListRoomRecyclerViewAdapter;
    @Inject
    lateinit var listRoomFavouriteRecyclerViewAdapter: IListRoomRecyclerViewAdapter;
    @Inject
    lateinit var listRoomNormalRecyclerViewAdapter: IListRoomRecyclerViewAdapter;

    private val bindingDataComponent: FragmentDataBindingComponent = FragmentDataBindingComponent(this);
    private lateinit var binding: FragmentSearchRoomsBinding;
    private var disposable: Disposable? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID, "");
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_rooms, container, false, bindingDataComponent);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listRoomInviteRecyclerViewAdapter.setDataBindingComponent(bindingDataComponent)
        listRoomFavouriteRecyclerViewAdapter.setDataBindingComponent(bindingDataComponent);
        listRoomNormalRecyclerViewAdapter.setDataBindingComponent(dataBindingComponent);
        listRoomInviteRecyclerViewAdapter.setOnItemClick { roomListUser, i ->
            roomListUser.room?.let {
                when (i) {
                    3 -> {
                        val intentRoom = Intent(this.context, RoomActivity::class.java);
                        intentRoom.putExtra(MXCActionBarActivity.EXTRA_MATRIX_ID, userId);
                        intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, it.id);
                        startActivity(intentRoom);
                    }
                }
            }
        }
        listRoomFavouriteRecyclerViewAdapter.setOnItemClick { roomListUser, i ->
            roomListUser.room?.let {
                when (i) {
                    3 -> {
                        val intentRoom = Intent(this.context, RoomActivity::class.java);
                        intentRoom.putExtra(MXCActionBarActivity.EXTRA_MATRIX_ID, userId);
                        intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, it.id);
                        startActivity(intentRoom);
                    }
                }
            }
        }
        listRoomNormalRecyclerViewAdapter.setOnItemClick { roomListUser, i ->
            roomListUser.room?.let {
                when (i) {
                    3 -> {
                        val intentRoom = Intent(this.context, RoomActivity::class.java);
                        intentRoom.putExtra(MXCActionBarActivity.EXTRA_MATRIX_ID, userId);
                        intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, it.id);
                        startActivity(intentRoom);
                    }
                }
            }
        }
        binding.recyclerViewInvites.adapter = listRoomInviteRecyclerViewAdapter.getAdapter();
        binding.recyclerViewFavourites.adapter = listRoomFavouriteRecyclerViewAdapter.getAdapter();
        binding.recyclerViewRooms.adapter = listRoomNormalRecyclerViewAdapter.getAdapter();
        viewModelFactory.getViewModel().getRoomInviteSearchResult().observe(viewLifecycleOwner, Observer {
            listRoomInviteRecyclerViewAdapter.getAdapter().submitList(it?.data);
        });
        viewModelFactory.getViewModel().getRoomFavouriteSearchResult().observe(viewLifecycleOwner, Observer {
            listRoomFavouriteRecyclerViewAdapter.getAdapter().submitList(it?.data);
        })
        viewModelFactory.getViewModel().getRoomNormalSearchResult().observe(viewLifecycleOwner, Observer {
            listRoomNormalRecyclerViewAdapter.getAdapter().submitList(it?.data);
        })

        binding.lifecycleOwner = viewLifecycleOwner;
    }

    // TODO: Rename method, update argument and hook method into UI event

    fun getSearchViewTextChange(): Observable<String>? {
        return listener?.getSearchViewTextChange();
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
        fun getSearchViewTextChange(): Observable<String>;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchRoomsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userId: String) =
                SearchRoomsFragment().apply {
                    arguments = Bundle().apply {
                        putString(USER_ID, userId);
                    }
                }
    }

    override fun selectedFragment(query: String): ISearchFragment {
        viewModelFactory.getViewModel().setQueryForSearch(query);
        disposable = getSearchViewTextChange()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe { t: String? ->
            t?.let { s ->
                viewModelFactory.getViewModel().setQueryForSearch(s);
            }
        };
        return this;
    }

    override fun getFragment(): Fragment {
        return this;
    }

    override fun unSelectedFragment() {
        disposable?.dispose();
    }
}
