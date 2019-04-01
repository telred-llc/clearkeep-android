package vmodev.clearkeep.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import im.vector.R
import kotlinx.android.synthetic.main.activity_login.*
import vmodev.clearkeep.fragments.LoginFragment

class LoginActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login);

        val loginFragment = LoginFragment();
        val transaction = supportFragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout_fragment_container, loginFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    override fun onLoginSuccess() {
        progress_bar.visibility = View.INVISIBLE;
        finish();
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE;
    }

    override fun hideLoading() {
        progress_bar.visibility = View.INVISIBLE;
    }
}
