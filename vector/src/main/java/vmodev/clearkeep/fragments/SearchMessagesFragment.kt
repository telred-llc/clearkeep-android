package vmodev.clearkeep.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment

import im.vector.R
import im.vector.databinding.FragmentSearchMessagesBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.adapters.ListSearchMessageRecyclerViewAdaptert
import vmodev.clearkeep.binding.FragmentDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.viewmodelobjects.MessageSearchText
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchViewModel
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SearchMessagesFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SearchMessagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SearchMessagesFragment : DaggerFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;
    @Inject
    lateinit var appExecutors: AppExecutors;

    val bindingDataComponent: FragmentDataBindingComponent = FragmentDataBindingComponent(this);
    lateinit var binding: FragmentSearchMessagesBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_messages, container, false, bindingDataComponent);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchViewModel = ViewModelProviders.of(this, viewModelFactory).get(AbstractSearchViewModel::class.java);
        binding.results = searchViewModel.getSearchMessageByTextResult();

        val listSearchAdapter = ListSearchMessageRecyclerViewAdaptert(appExecutors = appExecutors
                , dataBindingComponent = bindingDataComponent, diffCallback = object : DiffUtil.ItemCallback<MessageSearchText>() {
            override fun areItemsTheSame(p0: MessageSearchText, p1: MessageSearchText): Boolean {
                return p0.roomId == p1.roomId;
            }

            override fun areContentsTheSame(p0: MessageSearchText, p1: MessageSearchText): Boolean {
                return p0.name == p1.name && p0.avatarUrl == p1.avatarUrl && p0.content == p1.content && p0.updatedDate == p1.updatedDate;
            }
        }) { messageSearchText -> };
        binding.recyclerView.adapter = listSearchAdapter;
        searchViewModel.getSearchMessageByTextResult().observe(viewLifecycleOwner, Observer { t ->
            listSearchAdapter.submitList(t?.data);
        })
        getSearchViewTextChange()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe { t: String? ->
            t?.let { s -> searchViewModel.setKeywordSearchMessage(s) }
        }
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
         * @return A new instance of fragment SearchMessagesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SearchMessagesFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
