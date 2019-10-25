package vmodev.clearkeep.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorHomeActivity
import im.vector.databinding.ActivityHomeScreenBinding
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.databases.AbstractRoomUserJoinDao
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractHomeScreenActivityViewModel
import javax.inject.Inject

class HomeScreenActivity : DataBindingDaggerActivity(), IActivity {
    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractHomeScreenActivityViewModel>;

    private var doubleBackPressed: Boolean = false;

    lateinit var binding: ActivityHomeScreenBinding;
    lateinit var mxSession: MXSession;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window: Window = this.getWindow();
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_hint_text_color_light)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen, dataBinding.getDataBindingComponent());
        startIncomingCall();
        mxSession = Matrix.getInstance(this.applicationContext).defaultSession;
        binding.circleImageViewAvatar.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java);
            startActivity(intent);
        }

        binding.frameLayoutSearch.setOnClickListener { v ->
            val intent = Intent(this, SearchActivity::class.java);
            intent.putExtra(SearchActivity.USER_ID, mxSession.myUserId);
            startActivity(intent);
        }
        binding.user = viewModelFactory.getViewModel().getUserById();

        binding.lifecycleOwner = this;
        binding.buttonCreateConvention.setOnClickListener {
            val intent = Intent(this, NewRoomActivity::class.java)
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

        val navController = Navigation.findNavController(this@HomeScreenActivity, R.id.frame_layout_fragment_container);
        binding.bottomNavigationViewHomeScreen.setupWithNavController(navController);
    }

    private fun startIncomingCall() {
        if (intent.getStringExtra(EXTRA_CALL_SESSION_ID).isNullOrEmpty() || intent.getStringExtra(EXTRA_CALL_ID).isNullOrEmpty())
            return;
        val intentCall = Intent(this, CallViewActivity::class.java);
        intentCall.putExtra(CallViewActivity.EXTRA_MATRIX_ID, intent.getStringExtra(EXTRA_CALL_SESSION_ID));
        intentCall.putExtra(CallViewActivity.EXTRA_CALL_ID, intent.getStringExtra(EXTRA_CALL_ID));
        startActivity(intentCall);
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

    override fun onBackPressed() {
        if (doubleBackPressed) {
            super.onBackPressed()
            finish()
        }
        this.doubleBackPressed = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable {
            doubleBackPressed = false
        }, 3000)
    }

    companion object {
        const val WAITING_FOR_BACK_UP_KEY = 11352;
        const val EXTRA_MATRIX_ID = "EXTRA_MATRIX_ID"
        const val EXTRA_CALL_ID = "EXTRA_CALL_ID"
        const val EXTRA_CALL_SESSION_ID = "EXTRA_CALL_SESSION_ID";
    }

    override fun onResume() {
        super.onResume()
        doubleBackPressed = false
    }
}
