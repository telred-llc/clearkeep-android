package vmodev.clearkeep.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import im.vector.Matrix
import im.vector.activity.VectorHomeActivity
import io.reactivex.disposables.CompositeDisposable
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.bindingadapters.interfaces.IDataBindingComponent
import vmodev.clearkeep.dialogfragments.ReceivedShareFileDialogFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractDataBindingDaggerActivityViewModel
import java.util.*
import javax.inject.Inject

@Suppress("LeakingThis")
abstract class DataBindingDaggerActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var _viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var application: IApplication
    @Inject
    lateinit var dataBinding: IDataBindingComponent
    lateinit var dataBindingDaggerActivityViewModel: AbstractDataBindingDaggerActivityViewModel
    @Inject
    lateinit var clearKeepApplication: IApplication
    private var session: MXSession? = null

    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setTheme(application.getCurrentTheme())
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        session = Matrix.getInstance(applicationContext).defaultSession
        dataBindingDaggerActivityViewModel = ViewModelProvider(this, _viewModelFactory).get(AbstractDataBindingDaggerActivityViewModel::class.java)
        if (this is HomeScreenActivity)
            clearKeepApplication.checkVersion(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        dataBindingDaggerActivityViewModel.getLocalSettingsResult().observe(this, Observer {
            it?.data?.let {
                if (application.getCurrentTheme() != it.theme) {
                    application.setCurrentTheme(it.theme)
                    setTheme(it.theme)
                    recreate()
                }
            }
        })
        session?.let {
            dataBindingDaggerActivityViewModel.setTimeForGetTheme(Calendar.getInstance().timeInMillis)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RECEIVED_SEND_FILE && resultCode == Activity.RESULT_OK) {
            data?.let {
                if (it.hasExtra(VectorHomeActivity.EXTRA_SHARED_INTENT_PARAMS)) {
                    showSendFile(it.getParcelableExtra(VectorHomeActivity.EXTRA_SHARED_INTENT_PARAMS))
                }
            }
        }
    }

    private fun showSendFile(intentExtra: Intent) {
        session?.let {
            it.dataHandler.store?.let { mxStore ->
                if (mxStore.isReady) {
                    runOnUiThread {
                        val receivedShareDialogFragment = ReceivedShareFileDialogFragment.newInstance(it.myUserId, intentExtra)
                        receivedShareDialogFragment.show(supportFragmentManager, "")
                    }
                }
            }
        }
    }

    companion object {
        const val RECEIVED_SEND_FILE = 11046
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }
}
