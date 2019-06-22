package vmodev.clearkeep.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import im.vector.Matrix
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.viewmodels.interfaces.AbstractDataBindingDaggerActivityViewModel
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
        dataBindingDaggerActivityViewModel.getDeviceSettingsResult().observe(this, Observer {
            it?.data?.let {
                if (application.getCurrentTheme() != it.theme) {
                    application.setCurrentTheme(it.theme);
                    setTheme(it.theme);
                    recreate();
                }
            }
        })
        session?.let {
            dataBindingDaggerActivityViewModel.setDeviceSettingsId(it.myUserId);
        }

    }
}
