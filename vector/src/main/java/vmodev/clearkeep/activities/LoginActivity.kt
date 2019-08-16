package vmodev.clearkeep.activities

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.Toast
import im.vector.R
import im.vector.databinding.ActivityLoginBinding
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.*
import vmodev.clearkeep.viewmodelobjects.Status
import vmodev.clearkeep.viewmodels.interfaces.AbstractLoginActivityViewModel
import javax.inject.Inject

class LoginActivity : DataBindingDaggerActivity(), IActivity, LoginFragment.OnFragmentInteractionListener
        , SignUpFragment.OnFragmentInteractionListener, HandlerVerifyEmailFragment.OnFragmentInteractionListener
        , ForgotPasswordFragment.OnFragmentInteractionListener, ForgotPasswordVerifyEmailFragment.OnFragmentInteractionListener {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractLoginActivityViewModel>;
    private lateinit var binding: ActivityLoginBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login, dataBindingComponent);

        val loginFragment = LoginFragment.newInstance();
        changeFragment(loginFragment);
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    override fun onPressedSignUp() {
        viewModelFactory.getViewModel().getRegistrationFlowsResponseResult().observe(this, Observer {
            it?.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        it?.data?.let {
                            changeFragment(SignUpFragment.newInstance());
                            binding.progressBar.visibility = View.GONE;
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show();
                        binding.progressBar.visibility = View.GONE;
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE;
                    }
                }
            }
        })
    }

    override fun onPressSendToEmail(email: String, password: String) {
        changeFragment(ForgotPasswordVerifyEmailFragment.newInstance(email, password));
    }

    override fun onPressCancel() {
        changeFragment(LoginFragment.newInstance());
    }

    override fun onPressSignIn() {
        changeFragment(LoginFragment.newInstance());
    }

    override fun onPressedHaveAnAccount() {
        val loginFragment = LoginFragment.newInstance();
        changeFragment(loginFragment);
    }

    override fun onPressForgotPassword() {
        changeFragment(ForgotPasswordFragment.newInstance());
    }

    override fun onResetPasswordSuccess() {
        Toast.makeText(this, "Reset password success", Toast.LENGTH_LONG).show();
        changeFragment(LoginFragment.newInstance());
    }

    private fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.frame_layout_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    override fun onWaitingEmailValidation() {
        changeFragment(HandlerVerifyEmailFragment.newInstance());
    }

    override fun onPressedCancel() {
        changeFragment(SignUpFragment.newInstance());
    }
}
