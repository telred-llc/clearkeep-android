package vmodev.clearkeep.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import im.vector.R
import im.vector.databinding.FragmentHomeScreenBinding
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractHomeScreenFragmentViewModel
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeScreenFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HomeScreenFragment : DataBindingDaggerFragment(), IFragment {
    private var listener: OnFragmentInteractionListener? = null;
    @Inject
    lateinit var homeScreenFragmentViewModelFactory: IViewModelFactory<AbstractHomeScreenFragmentViewModel>;
    private lateinit var binding: FragmentHomeScreenBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_screen, container, false, dataBinding.getDataBindingComponent());
        setUpViewPage();
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.roomsDirectMessage = homeScreenFragmentViewModelFactory.getViewModel().getListRoomDirectMessage();
        binding.roomsRoomMessage = homeScreenFragmentViewModelFactory.getViewModel().getListRoomsMessage();
        binding.lifecycleOwner = viewLifecycleOwner;
        homeScreenFragmentViewModelFactory.getViewModel().setTypesDirectMessage(arrayOf(1, 1 or 64));
        homeScreenFragmentViewModelFactory.getViewModel().setTypesRoomsMessage(arrayOf(2, 2 or 64))
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }


    private fun setUpViewPage() {
        binding.tabLayoutHomeScreen.setupWithViewPager(binding.viewPagerHomeScreen);
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
