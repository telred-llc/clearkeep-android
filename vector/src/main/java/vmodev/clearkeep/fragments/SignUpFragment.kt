package vmodev.clearkeep.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import im.vector.BuildConfig
import im.vector.R
import im.vector.databinding.FragmentSignUpBinding
import im.vector.extensions.hideKeyboard
import im.vector.repositories.ServerUrlsRepository
import vmodev.clearkeep.activities.SplashActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.ultis.isEmailValid
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractSignUpFragmentViewModel
import java.util.regex.Pattern
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SignUpFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SignUpFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractSignUpFragmentViewModel>;

    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var binding: FragmentSignUpBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false, dataBinding.getDataBindingComponent());
        return binding.root;
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding.register = viewModelFactory.getViewModel().getRegisterResult();
        binding.nestedScrollview.setOnTouchListener { v, event ->
            hideKeyboard()
            return@setOnTouchListener true
        }
        binding.frameLayoutHaveAnAccount.setOnClickListener { v ->
            onPressedHaveAnAccount();
        };
        binding.buttonRegister.setOnClickListener { v ->
            var email = binding.textInputEditTextUsername.text.toString().trim();
            var username: String = "";
            if (email.isEmailValid()) {
                username = email.split("@")[0];
            } else {
                email = "";
                username = binding.textInputEditTextUsername.text.toString().trim();
            }

            val password = binding.textInputEditTextPassword.text.toString().trim();
            val confirmPassword = binding.textInputEditTextConfirmPassword.text.toString().trim();
            if (TextUtils.isEmpty(username)) {
                binding.textInputEditTextUsername.error = getString(R.string.error_empty_field_enter_user_name_or_email);
                return@setOnClickListener;
            } else if (TextUtils.isDigitsOnly(username[0].toString())) {
                showAlertDialog("Username error", "Numeric user IDs are reserved for guest users");
                return@setOnClickListener;
            } else {
                val expression = "^[a-z0-9.\\-_=/]+$"

                val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
                val matcher = pattern.matcher(username)
                if (!matcher.matches()) {
                    showAlertDialog("Username error", "User ID can only contain characters a-z,0-9, or '=_-./'");
                    return@setOnClickListener;
                }
            }
            if (TextUtils.isEmpty(password)) {
                binding.textInputEditTextPassword.error = getString(R.string.error_empty_field_your_password);
                return@setOnClickListener;
            }
            if (password.length < 6) {
                showAlertDialog("Password error", getString(R.string.password_too_short));
                return@setOnClickListener;
            }
            if (password.length > 15) {
                showAlertDialog("Password error", getString(R.string.password_too_long));
                return@setOnClickListener;
            }
            if (password.contains(" ")) {
                showAlertDialog("Password error", getString(R.string.password_contain_space));
                return@setOnClickListener;
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                binding.textInputEditTextConfirmPassword.error = getString(R.string.error_empty_field_your_password_confirm);
                return@setOnClickListener;
            }
            if (password.compareTo(confirmPassword) != 0) {
                showAlertDialog("Password error", "Password don't match");
                return@setOnClickListener;
            }
//            onPressedRegister(username, email, password);
            viewModelFactory.getViewModel().setDataForRegister(username, email, password);
        };
        viewModelFactory.getViewModel().getRegisterResult().observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.status == Status.SUCCESS) {
                    it.data?.let {
                        if (it.compareTo("onWaitingEmailValidation") == 0) {
                            onWaitingEmailValidation();
                        } else {
                            ServerUrlsRepository.saveServerUrls(this.context!!, BuildConfig.HOME_SERVER, BuildConfig.IDENTIFY_SERVER);
                            val intent = Intent(activity, SplashActivity::class.java);
                            intent.putExtra(SplashActivity.START_FROM_LOGIN, binding.textInputEditTextPassword.text.toString());
                            startActivity(intent);
                            activity?.finish();
                        }
                    }
                }
                if (it.status == Status.ERROR) {
                    Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show();
                }
            }
        })
        binding.lifecycleOwner = viewLifecycleOwner;
//        binding.nestedScrollview.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
////            hideKeyboard()
////        }
    }

    override fun getFragment(): Fragment {
        return this;
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onPressedHaveAnAccount() {
        listener?.onPressedHaveAnAccount()
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
        fun onPressedHaveAnAccount();

        fun onWaitingEmailValidation();
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignUpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                SignUpFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }

    private fun onWaitingEmailValidation() {
        listener?.onWaitingEmailValidation();
    }


    private fun showAlertDialog(title: String, message: String) {
        this.context?.let { AlertDialog.Builder(it).setTitle(title).setMessage(message).setNegativeButton("Close", null).show()};
    }
}
