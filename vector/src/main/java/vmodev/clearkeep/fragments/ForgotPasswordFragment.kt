package vmodev.clearkeep.fragments

import android.app.AlertDialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import im.vector.R
import im.vector.databinding.FragmentForgotPasswordBinding
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.ultis.isEmailValid
import vmodev.clearkeep.viewmodels.interfaces.AbstractForgotPasswordFragmentViewModel
import java.util.regex.Pattern
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ForgotPasswordFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ForgotPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ForgotPasswordFragment : DataBindingDaggerFragment(), IFragment {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractForgotPasswordFragmentViewModel>;

    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var binding: FragmentForgotPasswordBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false, dataBindingComponent);
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSendResetEmail.setOnClickListener {
            var email = binding.textInputEditTextUsername.text.toString().trim();

            val password = binding.textInputEditTextPassword.text.toString().trim();
            val confirmPassword = binding.textInputEditTextConfirmPassword.text.toString().trim();
            if (!email.isEmailValid()) {
                binding.textInputEditTextUsername.error = getString(R.string.error_empty_field_enter_email);
                return@setOnClickListener;
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
            onPressSendToEmail(email, password);
        }
        binding.buttonSignIn.setOnClickListener {
            onPressSignIn();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    private fun onPressSendToEmail(email: String, password: String) {
        listener?.onPressSendToEmail(email, password);
    }

    private fun onPressSignIn() {
        listener?.onPressSignIn();
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

    private fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this.context).setTitle(title).setMessage(message).setNegativeButton("Close", null).show();
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
        fun onPressSendToEmail(email: String, password: String)

        fun onPressSignIn();
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForgotPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                ForgotPasswordFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
