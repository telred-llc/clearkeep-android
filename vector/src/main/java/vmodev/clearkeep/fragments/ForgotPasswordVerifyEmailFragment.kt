package vmodev.clearkeep.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import im.vector.R
import im.vector.databinding.FragmentForgotPasswordBinding
import im.vector.databinding.FragmentForgotPasswordVerifyEmailBinding
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractForgotPasswordVerifyEmailFragmentViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val EMAIL = "EMAIL";
private const val PASSWORD = "PASSWORD"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ForgotPasswordVerifyEmailFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ForgotPasswordVerifyEmailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ForgotPasswordVerifyEmailFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractForgotPasswordVerifyEmailFragmentViewModel>;

    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var email: String;
    private lateinit var password: String;
    private lateinit var binding: FragmentForgotPasswordVerifyEmailBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            email = it.getString(EMAIL, "");
            password = it.getString(PASSWORD, "");
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password_verify_email, container, false, dataBindingComponent);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonCancel.setOnClickListener {
            onPressCancel();
        }
        viewModelFactory.getViewModel().getForgetPasswordResult().observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let {threePid ->
                            Observable.timer(30, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()).subscribe {
                                        viewModelFactory.getViewModel().setPasswordForResetPassword(password, threePid);
                                    }
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show();
                        onPressCancel();
                    }
                    else -> {

                    }
                }
            }
        });
        viewModelFactory.getViewModel().getResetPasswordResult().observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let {
                            onResetPasswordSuccess();
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show();
                        onPressCancel();
                    }
                    else -> {

                    }
                }
            }
        });
        viewModelFactory.getViewModel().setEmailForForgetPassword(email);
        binding.lifecycleOwner = viewLifecycleOwner;
    }

    // TODO: Rename method, update argument and hook method into UI event
    private fun onPressCancel() {
        listener?.onPressCancel();
    }

    private fun onResetPasswordSuccess() {
        listener?.onResetPasswordSuccess();
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
        fun onPressCancel();

        fun onResetPasswordSuccess();
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param email Parameter 1.
         * @param password Parameter 2.
         * @return A new instance of fragment ForgotPasswordVerifyEmailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(email: String, password: String) =
                ForgotPasswordVerifyEmailFragment().apply {
                    arguments = Bundle().apply {
                        putString(EMAIL, email)
                        putString(PASSWORD, password)
                    }
                }
    }
}
