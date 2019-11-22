package vmodev.clearkeep.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import im.vector.Matrix

import im.vector.R
import im.vector.VectorApp
import im.vector.activity.CommonActivityUtils
import im.vector.activity.MXCActionBarActivity
import im.vector.activity.VectorRoomActivity
import im.vector.databinding.FragmentSearchRoomsBinding
import im.vector.extensions.hideKeyboard
import im.vector.fragments.VectorPublicRoomsListFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_adapter_recent_room.*
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.activities.*
import vmodev.clearkeep.adapters.Interfaces.IListRoomDirectoryRecyclerViewAdapter
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListUserRecyclerViewAdapter
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.ISearchFragment
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchRoomsFragmentViewModel
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Named

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val USER_ID = "USER_ID";
private const val GO_TO_ROOM_CODE = 12432;

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
    private var currentRoomId: String = ""
    private var mSession: MXSession? = null

    @Inject
    lateinit var applcation: IApplication;

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractSearchRoomsFragmentViewModel>;
    @Inject
    lateinit var appExecutors: AppExecutors;
    @Inject
    lateinit var listRoomInviteRecyclerViewAdapter: IListRoomRecyclerViewAdapter;
//    @Inject
//    lateinit var listRoomDerectRecyclerViewAdapter: IListRoomRecyclerViewAdapter;

    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.SEARCH_ROOM)
    lateinit var listRoomDerectRecyclerViewAdapter: IListRoomRecyclerViewAdapter;

    @Inject
    @field:Named(value = IListRoomDirectoryRecyclerViewAdapter.SEARCH_ROOMDIRECTORY)
    lateinit var listRoomDirectoryRecyclerViewAdapter: IListRoomDirectoryRecyclerViewAdapter;

    @Inject
    @field:Named(value = IListRoomRecyclerViewAdapter.SEARCH_ROOM)
    lateinit var listRoomNormalRecyclerViewAdapter: IListRoomRecyclerViewAdapter;

    private lateinit var session: MXSession;
    private lateinit var binding: FragmentSearchRoomsBinding;
    private var disposable: Disposable? = null;
    private val ARG_MATRIX_ID = "SearchRoomsFragment.ARG_MATRIX_ID"
    private val LOG_TAG = SearchRoomsFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID, "");
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_rooms, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        val matrixId = args!!.getString(ARG_MATRIX_ID)
        mSession = Matrix.getInstance(activity)!!.getSession(matrixId)
        listRoomInviteRecyclerViewAdapter.getflag(1)
        binding.recyclerRoomDerectory.adapter = listRoomDirectoryRecyclerViewAdapter.getAdapter()
        listRoomDirectoryRecyclerViewAdapter.setOnItemClick { roomListUser, i ->
            roomListUser?.let {
                when (i) {
                    3 -> {
                        val room = mSession!!.dataHandler.getRoom(it.roomId, false)
                        if (null != room && room.isInvited) run {
                            Log.d(LOG_TAG, "manageRoom : the user is invited -> display the preview " + VectorApp.getCurrentActivity())
                            previewRoom(it.roomId);
                        } else if (null != room && room.isJoined) run {
                            Log.d(LOG_TAG, "manageRoom : the user joined the room -> open the room")
                            gotoRoom(it.roomId)
//                            val params = HashMap<String, Any>()
//                            params[VectorRoomActivity.EXTRA_MATRIX_ID] = mSession!!.myUserId
//                            params[VectorRoomActivity.EXTRA_ROOM_ID] = it.roomId
//
//                            if (!TextUtils.isEmpty(it.name)) {
//                                params[VectorRoomActivity.EXTRA_DEFAULT_NAME] = it.name
//                            }
//
//                            if (!TextUtils.isEmpty(it.topic)) {
//                                params[VectorRoomActivity.EXTRA_DEFAULT_TOPIC] = it.topic
//                            }
//
//                            CommonActivityUtils.goToRoomPage(activity!!, mSession, params)
                        } else {
                            // Display a preview by default.
                            Log.d(LOG_TAG, "manageRoom : display the preview")
                            previewJoin(it.roomId,it.name)
                        }


//                        val intentRoom = Intent(this.context, RoomActivity::class.java);
//                        intentRoom.putExtra(MXCActionBarActivity.EXTRA_MATRIX_ID, userId);
//                        intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, it.roomId);
//                        startActivity(intentRoom);
                    }
                }
            }
        }

        binding.recyclerDirects.adapter = listRoomDerectRecyclerViewAdapter.getAdapter();
        listRoomInviteRecyclerViewAdapter.setOnItemClick { roomListUser, i ->
            roomListUser.room?.let {
                when (i) {
                    3 -> {
                        val intentRoom = Intent(this.context, RoomActivity::class.java);
                        intentRoom.putExtra(MXCActionBarActivity.EXTRA_MATRIX_ID, userId);
                        intentRoom.putExtra(RoomActivity.EXTRA_ROOM_ID, it.id);
                        startActivity(intentRoom);
                    }
                    0 -> {
                        previewRoom(it.id);
                    }
                    1 -> {
                        previewRoom(it.id);
                    }
                    2 -> {
                        declineInvite(it.id);
                    }
                }
            }
        }
        listRoomDerectRecyclerViewAdapter.setOnItemClick { roomListUser, i ->
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
        binding.recyclerViewInvites.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding.recyclerViewRooms.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding.recyclerRoomDerectory.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        binding.recyclerDirects.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))

        binding.recyclerViewInvites.adapter = listRoomInviteRecyclerViewAdapter.getAdapter();
        binding.recyclerViewRooms.adapter = listRoomNormalRecyclerViewAdapter.getAdapter();
        viewModelFactory.getViewModel().getListRoomDirectory().observe(viewLifecycleOwner, Observer {
            listRoomDirectoryRecyclerViewAdapter.getAdapter().submitList(it?.data);
        })
//        viewModelFactory.getViewModel().getRoomDirectorySearchResult().observe(viewLifecycleOwner, Observer {
//            listRoomDirectoryRecyclerViewAdapter.getAdapter().submitList(it?.data);
//        })
        viewModelFactory.getViewModel().getRoomInviteSearchResult().observe(viewLifecycleOwner, Observer {
            listRoomInviteRecyclerViewAdapter.getAdapter().submitList(it?.data);
        });
        viewModelFactory.getViewModel().getDirectRoomNormalSearchResult().observe(viewLifecycleOwner, Observer {
            listRoomDerectRecyclerViewAdapter.getAdapter().submitList(it?.data);
        })
        viewModelFactory.getViewModel().getRoomNormalSearchResult().observe(viewLifecycleOwner, Observer {
            listRoomNormalRecyclerViewAdapter.getAdapter().submitList(it?.data);
        })
        viewModelFactory.getViewModel().joinRoomWithIdResult().observe(this.viewLifecycleOwner, Observer {
            it?.data?.let {
                if (it.id != currentRoomId) {
                    currentRoomId = it.id
                    gotoRoom(currentRoomId)
                }
            }
        });

        binding.rooms = viewModelFactory.getViewModel().getRoomNormalSearchResult()
        binding.invites = viewModelFactory.getViewModel().getRoomInviteSearchResult()
        binding.derects = viewModelFactory.getViewModel().getDirectRoomNormalSearchResult()
        binding.listRoomDirectory = viewModelFactory.getViewModel().getListRoomDirectory()
        binding.layoutSearch.setOnTouchListener { v, event ->
            hideKeyboard()
            return@setOnTouchListener true
        }
        binding.nestScroll.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            hideKeyboard()
        }
//        binding.recyclerDirects.setOnTouchListener { v, event ->
//            hideKeyboard()
//            return@setOnTouchListener true
//        }
//        binding.recyclerRoomDerectory.setOnTouchListener { v, event ->
//            hideKeyboard()
//            return@setOnTouchListener true
//        }
//        binding.recyclerViewInvites.setOnTouchListener { v, event ->
//            hideKeyboard()
//            return@setOnTouchListener true
//        }
//        binding.recyclerViewRooms.setOnTouchListener { v, event ->
//            hideKeyboard()
//            return@setOnTouchListener true
//        }
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

    private fun previewRoom(roomId: String) {
        val intent = Intent(this.context, PreviewInviteRoomActivity::class.java);
        intent.putExtra("ROOM_ID", roomId);
        startActivity(intent);
    }

    private fun previewJoin(roomId: String,roomName :String) {
        val intent = Intent(this.context, PreviewJoinActivity::class.java);
        intent.putExtra("ROOM_ID", roomId);
        intent.putExtra("ROOM_NAME", roomName);
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

}
