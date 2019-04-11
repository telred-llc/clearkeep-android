package vmodev.clearkeep.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import de.hdodenhof.circleimageview.CircleImageView
import im.vector.R
import im.vector.util.VectorUtils
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ROOM_ID = "ROOM_ID"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PreviewFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PreviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PreviewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var roomId: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var room: Room;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            roomId = it.getString(ROOM_ID)
        }
        room = onGetMXSession().dataHandler.getRoom(roomId);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_preview, container, false)
        VectorUtils.loadRoomAvatar(activity, onGetMXSession(), view.findViewById<CircleImageView>(R.id.circle_image_view_avatar), room);
        view.findViewById<Button>(R.id.button_join).setOnClickListener { v ->
            onJoinClick(room);
        };
        view.findViewById<Button>(R.id.button_decline).setOnClickListener { v -> onDeclineClick(room) }
        view.findViewById<Toolbar>(R.id.toolbar).setTitle(room.getRoomDisplayName(activity));
        view.findViewById<Toolbar>(R.id.toolbar).setNavigationIcon(R.drawable.ic_keyboard_arrow_left_greem_app_24dp);
        view.findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener { v -> kotlin.run { onNavigationOnClick() } };
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onJoinClick(room: Room) {
        listener?.onJoinClick(room)
    }

    fun onDeclineClick(room: Room) {
        listener?.onDeclineClick(room);
    }

    fun onGetMXSession(): MXSession {
        return listener?.onGetMXSession()!!;
    }
    fun onNavigationOnClick(){
        listener?.onNavigationOnClick();
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
        fun onJoinClick(room: Room);

        fun onDeclineClick(room: Room);
        fun onGetMXSession(): MXSession;
        fun onNavigationOnClick();
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PreviewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(roomId: String) =
                PreviewFragment().apply {
                    arguments = Bundle().apply {
                        putString(ROOM_ID, roomId)
                    }
                }
    }
}
