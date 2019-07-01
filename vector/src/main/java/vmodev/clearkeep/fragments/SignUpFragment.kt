package vmodev.clearkeep.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout

import im.vector.R
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.util.regex.Pattern

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SignUpFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SignUpFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

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
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        view.findViewById<FrameLayout>(R.id.frame_layout_have_an_account).setOnClickListener { v ->
            kotlin.run {
                onPressedHaveAnAccount();
            }
        };
        view.findViewById<Button>(R.id.button_register).setOnClickListener { v ->
            kotlin.run {
                var email = text_input_edit_text_username.text.toString().trim();
                var username: String = "";
                if (isEmailValid(email)) {
                    username = email.split("@")[0];
                } else {
                    email = "";
                    username = text_input_edit_text_username.text.toString().trim();
                }

                val password = text_input_edit_text_password.text.toString().trim();
                val confirmPassword = text_input_edit_text_confirm_password.text.toString().trim();
                if (TextUtils.isEmpty(username)) {
                    text_input_edit_text_username.setError(getString(R.string.error_empty_field_enter_user_name_or_email));
                    return@run;
                } else if (TextUtils.isDigitsOnly(username[0].toString())) {
                    showAlertDiaglong("Username error", "Numeric user IDs are reserved for guest users");
                    return@run;
                } else {
                    val expression = "^[a-z0-9.\\-_=/]+$"

                    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
                    val matcher = pattern.matcher(username)
                    if (!matcher.matches()) {
                        showAlertDiaglong("Username error", "User ID can only contain characters a-z,0-9, or '=_-./'");
                        return@run;
                    }
                }
                if (TextUtils.isEmpty(password)) {
                    text_input_edit_text_password.setError(getString(R.string.error_empty_field_your_password));
                    return@run;
                }
                if (password.length < 6) {
                    showAlertDiaglong("Password error", getString(R.string.password_too_short));
                    return@run;
                }
                if (password.length > 15) {
                    showAlertDiaglong("Password error", getString(R.string.password_too_long));
                    return@run;
                }
                if (password.contains(" ")) {
                    showAlertDiaglong("Password error", getString(R.string.password_contain_space));
                    return@run;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    text_input_edit_text_confirm_password.setError(getString(R.string.error_empty_field_your_password_confirm));
                    return@run;
                }
                if (password.compareTo(confirmPassword) != 0) {
                    showAlertDiaglong("Password error", "Password don't match");
                    return@run;
                }
                onPressedRegister(username, email, password);
            }
        };
        return view;
    }

    fun isEmailValid(email: String): Boolean {
        val regExpn = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")

        val pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)

        return if (matcher.matches())
            true
        else
            false
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
        fun onPressedHaveAnAccount()

        fun onPressedRegister(username: String, email: String, password: String);
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
        fun newInstance(param1: String, param2: String) =
                SignUpFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun onPressedRegister(username: String, email: String, password: String) {
        listener?.onPressedRegister(username, email, password);
    }


    private fun showAlertDiaglong(title: String, message: String) {
        AlertDialog.Builder(this.context).setTitle(title).setMessage(message).setNegativeButton("Close", null).show();
    }
}
