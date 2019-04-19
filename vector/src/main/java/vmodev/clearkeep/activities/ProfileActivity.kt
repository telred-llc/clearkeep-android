package vmodev.clearkeep.activities

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import dagger.android.DaggerActivity
import dagger.android.support.DaggerAppCompatActivity
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.databinding.ActivityProfileBinding
import im.vector.fragments.signout.SignOutBottomSheetDialogFragment
import im.vector.fragments.signout.SignOutViewModel
import im.vector.util.VectorUtils
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.viewmodels.UserViewModel
import javax.inject.Inject

class ProfileActivity : DaggerAppCompatActivity(), LifecycleOwner {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory;

    lateinit var mxSession: MXSession;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataBinding = DataBindingUtil.setContentView<ActivityProfileBinding>(this, R.layout.activity_profile);
        mxSession = Matrix.getInstance(this.applicationContext).defaultSession;
        setSupportActionBar(dataBinding.toolbar);
        supportActionBar!!.setTitle(R.string.profile);
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.setDisplayShowHomeEnabled(true);
        dataBinding.toolbar.setNavigationOnClickListener { v ->
            kotlin.run {
                onBackPressed();
            }
        }
        val userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java);
        userViewModel.setUserId(mxSession.myUserId);
        dataBinding.user = userViewModel.user;
        dataBinding.lifecycleOwner = this;
        VectorUtils.loadUserAvatar(this, mxSession, dataBinding.imageViewAvatar, mxSession.myUser);
        dataBinding.buttonSignOut.setOnClickListener{v -> kotlin.run { signOut() } }
    }
    private fun signOut() {

        if (SignOutViewModel.doYouNeedToBeDisplayed(mxSession)) {
            val signoutDialog = SignOutBottomSheetDialogFragment.newInstance(mxSession.getMyUserId())
            signoutDialog.onSignOut = Runnable { CommonActivityUtils.logout(this@ProfileActivity) }

            signoutDialog.show(supportFragmentManager, "SO")
        } else {
            // Display a simple confirmation dialog
            AlertDialog.Builder(this)
                    .setTitle(R.string.action_sign_out)
                    .setMessage(R.string.action_sign_out_confirmation_simple)
                    .setPositiveButton(R.string.action_sign_out) { dialog, which ->
                        CommonActivityUtils.logout(this@ProfileActivity)
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
        }
    }
}
