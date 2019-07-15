package vmodev.clearkeep.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import im.vector.BuildConfig
import im.vector.LoginHandler
import im.vector.R
import im.vector.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*
import org.matrix.androidsdk.HomeServerConnectionConfig
import org.matrix.androidsdk.rest.callback.ApiCallback
import org.matrix.androidsdk.rest.callback.SimpleApiCallback
import org.matrix.androidsdk.rest.model.MatrixError
import vmodev.clearkeep.activities.DemoEmptyActivity
import vmodev.clearkeep.activities.SplashActivity
import vmodev.clearkeep.fragments.Interfaces.IFragment
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LoginFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LoginFragment : DataBindingDaggerFragment(), IFragment {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var binding: FragmentLoginBinding;

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false, dataBindingComponent);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSignIn.setOnClickListener { v ->
            run {
                val password = edit_text_password.text.toString().trim();
                val username = edit_text_username.text.toString().trim();
                if (TextUtils.isEmpty(username)) {
                    edit_text_username.setError(getString(R.string.error_empty_field_enter_user_name));
                    return@run;
                }
                if (TextUtils.isEmpty(password)) {
                    edit_text_password.setError(getString(R.string.error_empty_field_your_password));
                    return@run;
                }

                onPressedLogin();
                val homeServerConnectionConfig = HomeServerConnectionConfig.Builder().withHomeServerUri(Uri.parse(BuildConfig.HOME_SERVER))
                        .withIdentityServerUri(Uri.parse("https://matrix.org")).build();
                val loginHandler = LoginHandler();
                loginHandler.login(this.context, homeServerConnectionConfig, edit_text_username.text.toString(), null, null,
                        edit_text_password.text.toString(), object : ApiCallback<Void> {
                    override fun onSuccess(p0: Void?) {
                        gotoHomeActivity();
                    }

                    override fun onNetworkError(e: Exception?) {
                        onPressedClose();
                        if (e != null) {
                            showAlertDiaglong("Sign in error", e.message!!)
                        }
                    }

                    override fun onMatrixError(e: MatrixError?) {
                        onPressedClose();
                        if (e != null) {
                            showAlertDiaglong("Sign in error", e.message)
                        };
                    }

                    override fun onUnexpectedError(e: Exception?) {
                        onPressedClose();
                        if (e != null) {
                            showAlertDiaglong("Sign in error", e.message!!)
                        }
                    }
                });
            }
        };
        binding.buttonSignUp.setOnClickListener { v ->
            run {
                onPressedSignUp();
            }
        }
    }

    private fun showAlertDiaglong(title: String, message: String) {
        AlertDialog.Builder(this.context).setTitle(title).setMessage(message).setNegativeButton("Close", null).show();
    }

    private fun gotoHomeActivity() {
        val intent = Intent(this.context, SplashActivity::class.java);
        startActivity(intent);
        onLoginSuccess();
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onLoginSuccess() {
        listener?.onLoginSuccess()
    }

    fun onPressedLogin() {
        listener?.showLoading();
    }

    fun onPressedClose() {
        listener?.hideLoading();
    }

    fun onPressedSignUp() {
        listener?.onPressedSignUp();
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
        fun onLoginSuccess()
        fun showLoading();
        fun hideLoading();
        fun onPressedSignUp();
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                LoginFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
