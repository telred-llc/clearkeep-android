package vmodev.clearkeep.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import im.vector.R
import im.vector.databinding.FragmentLoginBinding
import vmodev.clearkeep.activities.SplashActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.ultis.isEmailValid
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractLoginFragmentViewModel
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

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

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractLoginFragmentViewModel>;

    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var binding: FragmentLoginBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.user = viewModelFactory.getViewModel().getLoginResult();
        viewModelFactory.getViewModel().getLoginResult().observe(viewLifecycleOwner, Observer {
            it?.let {resource ->
                if (resource.status == Status.SUCCESS) {
                    resource.data?.let {
                        gotoHomeActivity();
                    }
                }
                if (resource.status == Status.ERROR) {
                    this.context?.let { AlertDialog.Builder(it).setTitle(R.string.sign_in_error).setMessage(resource.message).setNegativeButton(R.string.close, null).show(); }
                }
            }
        });
        binding.textForgotPassword.setOnClickListener {
            onPressForgotPassword();
        };
        binding.buttonSignIn.setOnClickListener { v ->
            run {
                val password = binding.editTextPassword.text.toString().trim();
                val username = binding.editTextUsername.text.toString().trim();
                if (TextUtils.isEmpty(username)) {
                    binding.editTextUsername.setError(getString(R.string.error_empty_field_enter_user_name_or_email));
                    return@run;
                }
                if (TextUtils.isEmpty(password)) {
                    binding.editTextPassword.setError(getString(R.string.error_empty_field_your_password));
                    return@run;
                }

                if (username.isEmailValid())
                    viewModelFactory.getViewModel().setUserNamePasswordForLogin(username.split("@")[0], password);
                else
                    viewModelFactory.getViewModel().setUserNamePasswordForLogin(username, password);
            }
        };
        binding.buttonSignUp.setOnClickListener { v ->
            run {
                onPressedSignUp();
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner;
    }

    private fun gotoHomeActivity() {
        val intent = Intent(this.context, SplashActivity::class.java);
        intent.putExtra(SplashActivity.START_FROM_LOGIN, binding.editTextPassword.text.toString());
        startActivity(intent);
        activity?.finish();
    }

    // TODO: Rename method, update argument and hook method into UI event

    private fun onPressedSignUp() {
        listener?.onPressedSignUp();
    }

    private fun onPressForgotPassword() {
        listener?.onPressForgotPassword();
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
        fun onPressedSignUp();
        fun onPressForgotPassword();
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
        fun newInstance() =
                LoginFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
