package vmodev.clearkeep.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorHomeActivity
import im.vector.databinding.ActivityHomeScreenBinding
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.bindingadapters.DataBindingComponentImplement
import vmodev.clearkeep.bindingadapters.interfaces.IDataBindingComponent
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IFragmentFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.ContactsFragment
import vmodev.clearkeep.fragments.HomeScreenFragment
import vmodev.clearkeep.fragments.ListRoomFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractHomeScreenActivityViewModel
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class HomeScreenActivity : DataBindingDaggerActivity(), HomeScreenFragment.OnFragmentInteractionListener,
        ContactsFragment.OnFragmentInteractionListener
        , ListRoomFragment.OnFragmentInteractionListener, IActivity {
    @Inject
    @field:Named(IFragmentFactory.CONTACTS_FRAGMENT)
    lateinit var contactsFragmentFactory: IFragmentFactory;
    @Inject
    @field:Named(IFragmentFactory.LIST_ROOM_FRAGMENT)
    lateinit var listRoomFragmentFactory: IFragmentFactory;
    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractHomeScreenActivityViewModel>;

    lateinit var binding: ActivityHomeScreenBinding;
    lateinit var mxSession: MXSession;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen, dataBinding.getDataBindingComponent());
        startIncomingCall();
        mxSession = Matrix.getInstance(this.applicationContext).defaultSession;
        binding.bottomNavigationViewHomeScreen.setOnNavigationItemSelectedListener { menuItem ->
            run {
                when (menuItem.itemId) {
                    R.id.action_home -> {
                        switchFragment(listRoomFragmentFactory.createNewInstance().getFragment());
                    };
                    R.id.action_contacts -> {
                        switchFragment(contactsFragmentFactory.createNewInstance().getFragment());
                    };
                }
                return@run true;
            }
        };
        binding.circleImageViewAvatar.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java);
            startActivity(intent);
        }

        switchFragment(listRoomFragmentFactory.createNewInstance().getFragment());

        binding.frameLayoutSearch.setOnClickListener { v ->
            val intent = Intent(this, SearchActivity::class.java);
            intent.putExtra(SearchActivity.USER_ID, mxSession.myUserId);
            startActivity(intent);
        }
        binding.user = viewModelFactory.getViewModel().getUserById();

        binding.lifecycleOwner = this;
        binding.buttonCreateConvention.setOnClickListener {
            val intent = Intent(this, FindAndCreateNewConversationActivity::class.java)
            startActivity(intent);
        }
        viewModelFactory.getViewModel().setValueForUserById(mxSession.myUserId);

        if (intent.hasExtra(VectorHomeActivity.EXTRA_SHARED_INTENT_PARAMS)) {
            val intentExtra: Intent = intent.getParcelableExtra(VectorHomeActivity.EXTRA_SHARED_INTENT_PARAMS);
            if (mxSession.dataHandler.store.isReady) {
                runOnUiThread {
                    CommonActivityUtils.sendFilesTo(this@HomeScreenActivity, intentExtra)
                }
            } else {
//                mSharedFilesIntent = sharedFilesIntent
            }
        }

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).setMessage(R.string.exit_app_dialog_content)
                .setNegativeButton(R.string.yes) { dialogInterface, i -> finish() }
                .setPositiveButton(R.string.no) { dialogInterface, i -> }
                .show();
    }

    private fun startIncomingCall() {
        if (intent.getStringExtra(EXTRA_CALL_SESSION_ID).isNullOrEmpty() || intent.getStringExtra(EXTRA_CALL_ID).isNullOrEmpty())
            return;
        val intentCall = Intent(this, CallViewActivity::class.java);
        intentCall.putExtra(CallViewActivity.EXTRA_MATRIX_ID, intent.getStringExtra(EXTRA_CALL_SESSION_ID));
        intentCall.putExtra(CallViewActivity.EXTRA_CALL_ID, intent.getStringExtra(EXTRA_CALL_ID));
        startActivity(intentCall);
    }

    private fun switchFragment(fragment: Fragment) {
        Handler().post {
            val transaction = supportFragmentManager.beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.replace(R.id.frame_layout_fragment_container, fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    override fun onFragmentInteraction(uri: Uri) {
    }

    override fun getActivity(): FragmentActivity {
        return this;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == WAITING_FOR_BACK_UP_KEY && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Successfully", Toast.LENGTH_LONG).show();
        }
    }

    companion object {
        const val WAITING_FOR_BACK_UP_KEY = 11352;
        const val EXTRA_MATRIX_ID = "EXTRA_MATRIX_ID"
        const val EXTRA_CALL_ID = "EXTRA_CALL_ID"
        const val EXTRA_CALL_SESSION_ID = "EXTRA_CALL_SESSION_ID";
    }
}
