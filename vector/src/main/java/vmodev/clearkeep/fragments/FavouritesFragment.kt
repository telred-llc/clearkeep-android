package vmodev.clearkeep.fragments

import android.content.Context
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
class FavouritesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    // You can declare variable to pass from activity is here

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var recyclerView: RecyclerView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Deserialize is here
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourites2, container, false)
        recyclerView = view.findViewById(R.id.recycler_view_list_favourites);
        val layoutManager = LinearLayoutManager(this.context);
        val dividerItemDecoration = DividerItemDecoration(this.context, layoutManager.orientation);
        recyclerView.layoutManager = layoutManager;
        recyclerView.addItemDecoration(dividerItemDecoration);
        val directMessageRecyclerViewAdapter = DirectMessageRecyclerViewAdapter(onGetRooms(),ArrayList(), onGetMXSession());
        directMessageRecyclerViewAdapter.publishSubject.subscribe { r ->kotlin.run { onClickItem(r.room!!) } };
        recyclerView.adapter = directMessageRecyclerViewAdapter;
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onGetMXSession(): MXSession {
        return listener?.onGetMXSession()!!;
    }
    fun onGetRooms() : List<Room>{
        return listener?.onGetListFavourites()!!;
    }
    fun onClickItem(room : Room){
        listener?.onClickItem(room);
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
        fun onGetListFavourites() : List<Room>;
        fun onClickItem(room : Room);
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
