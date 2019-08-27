package vmodev.clearkeep.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jakewharton.rxbinding2.support.design.widget.dismisses

import im.vector.R
import im.vector.databinding.FragmentForgotPasswordBinding
import im.vector.databinding.FragmentForgotPasswordVerifyEmailBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.rest.model.login.ThreePidCredentials
import org.matrix.androidsdk.rest.model.pid.ThreePid
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
    private var checkError: Boolean = false
    private lateinit var binding: FragmentForgotPasswordVerifyEmailBinding;
    private var currentThreePid: ThreePid? = null;
    private var disposable: Disposable? = null
    private var alertDialog: AlertDialog? = null
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
        val onClickListener = View.OnClickListener {
            alertDialog = AlertDialog.Builder(getActivity()!!)
                    .setTitle(R.string.cancel_request)
                    .setCancelable(false)
                    .setNegativeButton(R.string.no) { _, _ ->
                        if (checkError == true) {
                            onPressCancel();
                        } else {

                        }

                    }
                    .setPositiveButton(R.string.yes) { _, _ ->
                        //nop
                        disposable?.dispose()
                        onPressCancelForgot()
                    }.show()
        }
        binding.buttonCancel.setOnClickListener(onClickListener)


//            onPressCancel();
        viewModelFactory.getViewModel().getForgetPasswordResult().observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { threePid ->
                            currentThreePid = threePid;
                            viewModelFactory.getViewModel().setPasswordForResetPassword(password, threePid);
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show();
                        if (alertDialog == null || !alertDialog!!.isShowing) {
                            onPressCancel();
                        } else {
                            checkError = true
                        }
                    }
                    Status.LOADING -> {

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
                            alertDialog?.dismiss()
                        }
                    }
                    Status.ERROR -> {
                        if (TextUtils.equals(it.message, "M_UNAUTHORIZED")) {
                            currentThreePid?.let { threePid ->
                                disposable = Observable.timer(5, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe { viewModelFactory.getViewModel().setPasswordForResetPassword(password, threePid) }
                                binding.buttonCancel.setOnClickListener(onClickListener)
                            }
                        } else {
                            Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show();
                            if (alertDialog == null || !alertDialog!!.isShowing) {
                                onPressCancel();
                            } else {
                                checkError = true
                            }

                        }
                    }
                    Status.LOADING -> {
                        binding.buttonCancel.setOnClickListener(null)
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

    private fun onPressCancelForgot() {
        listener?.onCancelForgot();
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

        fun onCancelForgot()
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

    override fun onResume() {
        super.onResume()
        checkError = false
    }

}
