package vmodev.clearkeep.activities

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import im.vector.Matrix
import im.vector.activity.VectorHomeActivity
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.dialogfragments.ReceivedShareFileDialogFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractDataBindingDaggerActivityViewModel
import java.util.*
import javax.inject.Inject

@Suppress("LeakingThis")
abstract class DataBindingDaggerActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var _viewModelFactory: ViewModelProvider.Factory;
    @Inject
    lateinit var application: IApplication;
    lateinit var dataBindingDaggerActivityViewModel: AbstractDataBindingDaggerActivityViewModel;

    private var session: MXSession? = null;

    val dataBindingComponent: ActivityDataBindingComponent
        get() = ActivityDataBindingComponent(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(application.getCurrentTheme());
        session = Matrix.getInstance(applicationContext).defaultSession;
        dataBindingDaggerActivityViewModel = ViewModelProviders.of(this, _viewModelFactory).get(AbstractDataBindingDaggerActivityViewModel::class.java);

    }

    override fun onResume() {
        super.onResume()
        dataBindingDaggerActivityViewModel.getLocalSettingsResult().observe(this, Observer {
            it?.data?.let {
                if (application.getCurrentTheme() != it.theme) {
                    application.setCurrentTheme(it.theme);
                    setTheme(it.theme);
                    recreate();
                }
            }
        })
        session?.let {
            dataBindingDaggerActivityViewModel.setTimeForGetTheme(Calendar.getInstance().timeInMillis);
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RECEIVED_SEND_FILE && resultCode == Activity.RESULT_OK) {
            data?.let {
                if (it.hasExtra(VectorHomeActivity.EXTRA_SHARED_INTENT_PARAMS)) {
                    showSendFile(it.getParcelableExtra(VectorHomeActivity.EXTRA_SHARED_INTENT_PARAMS));
                }
            }
        }
    }

    private fun showSendFile(intentExtra: Intent) {
        session?.let {
            if (it.dataHandler.store.isReady) {
                runOnUiThread {
                    val receivedShareDialogFragment = ReceivedShareFileDialogFragment.newInstance(it.myUserId, intentExtra);
                    receivedShareDialogFragment.show(supportFragmentManager, "");
                }
            }
        }
    }

    companion object {
        const val RECEIVED_SEND_FILE = 11046;
    }
}
