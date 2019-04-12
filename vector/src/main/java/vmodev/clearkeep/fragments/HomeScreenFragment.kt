package vmodev.clearkeep.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import im.vector.R
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import org.matrix.androidsdk.data.Room
import vmodev.clearkeep.adapters.HomeScreenPagerAdapter
import vmodev.clearkeep.viewmodelobjects.Status

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val DIRECT_MESSAGE = "DIRECT_MESSAGE";
private const val ROOMS = "ROOMS"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeScreenFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HomeScreenFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null;

    private lateinit var viewPager: ViewPager;
    private lateinit var tabLayout: TabLayout;
    private lateinit var textViewNotifyRoom: TextView;
    private lateinit var textViewNotifyDirect: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_screen, container, false)
        viewPager = view.findViewById<ViewPager>(R.id.view_pager_home_screen);
        tabLayout = view.findViewById(R.id.tab_layout_home_screen);
        textViewNotifyRoom = view.findViewById(R.id.text_view_notify_room);
        textViewNotifyDirect = view.findViewById(R.id.text_view_notify_direct);
        setUpViewPage();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    fun getRoomNotifyCount(): Int {
        return listener?.getRoomNotifyCount()!!;
    }

    fun getDirectNotifyCount(): Int {
        return listener?.getDirectNotifyCount()!!;
    }
    fun handleDataChange(): PublishSubject<Status> {
        return listener?.handleDataChange()!!;
    }
    fun upData() {
        return listener?.updateData()!!;
    }

    private fun setUpViewPage() {
        val fragments: Array<Fragment> = arrayOf(DirectMessageFragment.newInstance(), RoomFragment.newInstance());
        viewPager.adapter = HomeScreenPagerAdapter(childFragmentManager, fragments);
        tabLayout.setupWithViewPager(viewPager);
        handleDataChange().subscribe { t -> kotlin.run {
            if (getRoomNotifyCount() > 0){
                textViewNotifyRoom.visibility = View.VISIBLE;
                textViewNotifyRoom.text = getRoomNotifyCount().toString();
            }
            else{
                textViewNotifyRoom.visibility = View.INVISIBLE;
            }
            if (getDirectNotifyCount() > 0){
                textViewNotifyDirect.visibility = View.VISIBLE;
                textViewNotifyDirect.text = getDirectNotifyCount().toString();
            }
            else{
                textViewNotifyDirect.visibility = View.INVISIBLE;
            }
        } };
        upData();
    }

    override fun onResume() {
        super.onResume()
        upData();
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
        fun onFragmentInteraction(uri: Uri)
        fun updateData();
        fun getRoomNotifyCount(): Int;
        fun getDirectNotifyCount(): Int;
        fun handleDataChange(): PublishSubject<Status>;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeScreenFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                HomeScreenFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
