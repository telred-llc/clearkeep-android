package vmodev.clearkeep.applications

import android.annotation.SuppressLint
import android.app.Application
import android.support.v7.app.AppCompatDelegate
import android.util.Log
import android.widget.Toast
import dagger.android.AndroidInjector
import im.vector.Matrix
import im.vector.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.aes.interfaces.ICrypto
import vmodev.clearkeep.autokeybackups.AutoKeyBackup
import vmodev.clearkeep.autokeybackups.interfaces.IAutoKeyBackup
import vmodev.clearkeep.databases.ClearKeepDatabase
import vmodev.clearkeep.di.DaggerAppComponent
import vmodev.clearkeep.matrixsdk.interfaces.IMatrixEventHandler
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.rests.models.responses.PassphraseResponse
import javax.inject.Inject


class ClearKeepApplication : DaggerVectorApp(), IApplication {

    @Inject
    lateinit var matrixEventHandler: IMatrixEventHandler;
    @Inject
    lateinit var database: ClearKeepDatabase;
    @Inject
    lateinit var autoKeyBackup: IAutoKeyBackup;

    private var session: MXSession? = null;

    private var currentTheme: Int = R.style.LightTheme;

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { throwable -> Log.d("RX Throw: ", throwable.message) }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    override fun applicationInjector(): AndroidInjector<out ClearKeepApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }

    override fun setEventHandler() {
        session = Matrix.getInstance(this).defaultSession;
        session?.let { it.dataHandler.addListener(matrixEventHandler.getMXEventListener(it)) }

    }

    override fun removeEventHandler() {
        session?.let { it.dataHandler.removeListener(matrixEventHandler.getMXEventListener(it)) }
    }

    override fun getCurrentTheme(): Int {
        return currentTheme;
    }

    override fun setCurrentTheme(theme: Int) {
        currentTheme = theme;
    }

    override fun getApplication(): Application {
        return this;
    }

    override fun startAutoKeyBackup(password: String?) {
        session?.let { autoKeyBackup.startAutoKeyBackup(it.myUserId, password) }
    }
}