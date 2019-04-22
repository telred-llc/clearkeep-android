package vmodev.clearkeep.fragments

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.DaggerFragment
import im.vector.R
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room
import vmodev.clearkeep.adapters.DirectMessageRecyclerViewAdapter
import vmodev.clearkeep.viewmodelobjects.Status
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ROOMS = "ROOMS"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RoomFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RoomFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RoomFragment : dagger.android.support.DaggerFragment() {
    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var recyclerView: RecyclerView;
    private lateinit var session: MXSession;

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_room, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_list_room);
        val layoutManager = LinearLayoutManager(this.context);
        val dividerItemDecoration = DividerItemDecoration(this.context, layoutManager.orientation);
        recyclerView.layoutManager = layoutManager;
        recyclerView.addItemDecoration(dividerItemDecoration);
        setUpData();
        session = this!!.onGetMXSession()!!;
        return view;
    }

    private fun setUpData() {
        val adapter = DirectMessageRecyclerViewAdapter(onGetListRooms(), onGetListRoomInvitation(), onGetMXSession()!!, activity!!);
        recyclerView.adapter = adapter;
        adapter.updateData();
        handleDataChange().subscribe { t: Status? ->
            kotlin.run {
                if (t == Status.SUCCESS) {
                    adapter.rooms = onGetListRooms();
                    adapter.invitations = onGetListRoomInvitation();
                    adapter.updateData();
                    adapter.notifyDataSetChanged();
                }
            }
        };

        adapter.publishSubject.subscribe { t ->
            kotlin.run {
                when (t.clickType) {
                    0 -> onClickItem(t.room!!);
                    1 -> onClickItemDecline(t.room!!)
                    2 -> onClickJoinRoom(t.room!!)
                    3 -> onClickItemPreview(t.room!!);
                    else -> onClickItem(t.room!!);
                }
            }
        };
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onGetMXSession(): MXSession {
        return listener?.onGetMXSession()!!;
    }

    fun onGetListRooms(): List<Room> {
        return listener?.onGetListRooms()!!;
    }

    fun onClickItem(room: Room) {
        listener?.onClickItem(room);
    }

    fun onGetListRoomInvitation(): List<Room> {
        return listener?.onGetListRoomInvitation()!!;
    }

    fun handleDataChange(): PublishSubject<Status> {
        return listener?.handleDataChange()!!;
    }

    fun onClickJoinRoom(room: Room) {
        listener?.onClickItemJoin(room);
    }

    fun onClickItemDecline(room: Room) {
        listener?.onClickItemDecline(room);
    }

    fun onClickItemPreview(room: Room) {
        listener?.onClickItemPreview(room);
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
        fun onGetMXSession(): MXSession;

        fun onGetListRooms(): List<Room>;
        fun onGetListRoomInvitation(): List<Room>;
        fun onClickItem(room: Room);
        fun handleDataChange(): PublishSubject<Status>;
        fun onClickItemJoin(room: Room);
        fun onClickItemDecline(room: Room);
        fun onClickItemPreview(room: Room);
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RoomFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                RoomFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
