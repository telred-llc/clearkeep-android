package vmodev.clearkeep.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import im.vector.R
import im.vector.databinding.FragmentSearchPeopleBinding
import im.vector.extensions.hideKeyboard
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.activities.UserInformationActivity
import vmodev.clearkeep.adapters.ListUserRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.ISearchFragment
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchPeopleFragmentViewModel
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SearchPeopleFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SearchPeopleFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SearchPeopleFragment : DataBindingDaggerFragment(), ISearchFragment {
    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractSearchPeopleFragmentViewModel>;
    @Inject
    lateinit var appExecutors: AppExecutors;

    private lateinit var binding: FragmentSearchPeopleBinding;
    private var disposable: Disposable? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_people, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        val listUserAdapter = ListUserRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(p0: User, p1: User): Boolean {
                return p0.id == p1.id;
            }

            override fun areContentsTheSame(p0: User, p1: User): Boolean {
                return p0.name == p1.name && p0.avatarUrl == p1.avatarUrl;
            }
        }, dataBinding = dataBinding) { user ->
            activity?.let {
                val intentUserProfile = Intent(it, UserInformationActivity::class.java);
                intentUserProfile.putExtra(UserInformationActivity.USER_ID, user.id);
                startActivity(intentUserProfile);
            }
        }
        binding.recyclerView.adapter = listUserAdapter;
        binding.users = viewModelFactory.getViewModel().getSearchResult();
        viewModelFactory.getViewModel().getSearchResult().observe(viewLifecycleOwner, Observer { t -> listUserAdapter.submitList(t?.data) });
        binding.recyclerView.setOnTouchListener { v, event ->
            hideKeyboard()
            return@setOnTouchListener true
        }
        binding.lifecycleOwner = viewLifecycleOwner;

    }

    override fun onResume() {
        super.onResume()

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
         * @return A new instance of fragment SearchPeopleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                SearchPeopleFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }

    override fun selectedFragment(query: String): ISearchFragment {
        viewModelFactory.getViewModel().setQueryForSearch(query);
        disposable = getSearchViewTextChange()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe { t: String? ->
            t?.let { s ->
                viewModelFactory.getViewModel().setQueryForSearch(query);
            }
        }
        return this;
    }

    override fun getFragment(): Fragment {
        return this;
    }

    override fun unSelectedFragment() {
        disposable?.dispose();
    }
}
