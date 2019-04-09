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

import im.vector.R
import org.matrix.androidsdk.data.Room
import vmodev.clearkeep.adapters.HomeScreenPagerAdapter

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

    private lateinit var viewPager : ViewPager;
    private lateinit var tabLayout : TabLayout;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home_screen, container, false)
        viewPager = view.findViewById<ViewPager>(R.id.view_pager_home_screen);
        tabLayout = view.findViewById(R.id.tab_layout_home_screen);
        setUpViewPage();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    private fun setUpViewPage(){
        val fragments : Array<Fragment> = arrayOf(DirectMessageFragment.newInstance(), RoomFragment.newInstance());
        viewPager.adapter = HomeScreenPagerAdapter(childFragmentManager, fragments);
        tabLayout.setupWithViewPager(viewPager);
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
