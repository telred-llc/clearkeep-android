package vmodev.clearkeep.fragments

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
import im.vector.R
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room
import vmodev.clearkeep.adapters.DirectMessageRecyclerViewAdapter

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
class RoomFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var rooms: Array<Room>? = null
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var recyclerView: RecyclerView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            rooms = it.getSerializable(ROOMS) as Array<Room>;
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
        val directMessageRecyclerViewAdapter = DirectMessageRecyclerViewAdapter(this!!.rooms!!, onGetMXSession());
        recyclerView.adapter = directMessageRecyclerViewAdapter;
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onGetMXSession() : MXSession {
        return listener?.onGetMXSession()!!;
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
        fun onGetMXSession() : MXSession;
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
        fun newInstance(rooms : Array<Room>) =
                RoomFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ROOMS, rooms);
                    }
                }
    }
}
