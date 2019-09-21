package vmodev.clearkeep.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import im.vector.R
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import im.vector.BuildConfig
import im.vector.databinding.FragmentHandlerVerifyEmailBinding
import im.vector.repositories.ServerUrlsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.activities.SplashActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractHandlerVerifyEmailFragmentViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HandlerVerifyEmailFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HandlerVerifyEmailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HandlerVerifyEmailFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractHandlerVerifyEmailFragmentViewModel>;

    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var binding: FragmentHandlerVerifyEmailBinding;
    private var disposable: Disposable? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_handler_verify_email, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposable = io.reactivex.Observable.interval(0, 10, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewModelFactory.getViewModel().getRegistrationFlowsResponseResult().observe(viewLifecycleOwner, Observer {
                        it?.let {
                            when (it.status) {
                                Status.SUCCESS -> {
                                    binding.progressBar.visibility = View.GONE;
                                    ServerUrlsRepository.saveServerUrls(this.context!!, BuildConfig.HOME_SERVER, BuildConfig.IDENTIFY_SERVER);
                                    val intent = Intent(activity, SplashActivity::class.java);
                                    startActivity(intent);
                                    activity?.finish();
                                }
                                Status.ERROR -> {
                                    binding.progressBar.visibility = View.GONE;
                                    Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show();
                                    onButtonPressed();
                                }
                                Status.LOADING -> {
                                    binding.progressBar.visibility = View.VISIBLE;
                                }
                            }
                        }
                    });
                }
        binding.buttonCancel.setOnClickListener { v ->
            run {
                AlertDialog.Builder(this.context).setTitle("Cancel Registration").setMessage("Are you sure to cancel this registration?").setPositiveButton("Yes") { dialog, which ->
                    run {
                        disposable?.dispose();
                        onButtonPressed()
                    }
                }.setNegativeButton("No", null).show();
            }
        };
        binding.lifecycleOwner = viewLifecycleOwner;
    }

    // TODO: Rename method, update argument and hook method into UI event
    private fun onButtonPressed() {
        listener?.onPressedCancel();
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
        disposable?.dispose();
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
        fun onPressedCancel();
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HandlerVerifyEmailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                HandlerVerifyEmailFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
