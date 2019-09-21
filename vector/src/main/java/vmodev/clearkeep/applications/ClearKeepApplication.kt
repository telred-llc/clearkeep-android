package vmodev.clearkeep.applications

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import im.vector.Matrix
import im.vector.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.autokeybackups.interfaces.IAutoKeyBackup
import vmodev.clearkeep.databases.AbstractRoomDao
import vmodev.clearkeep.databases.ClearKeepDatabase
import vmodev.clearkeep.di.DaggerAppComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.MatrixEventHandler
import vmodev.clearkeep.matrixsdk.interfaces.IMatrixEventHandler
import vmodev.clearkeep.repositories.KeyBackupRepository
import vmodev.clearkeep.repositories.SignatureRepository
import vmodev.clearkeep.repositories.UserRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ClearKeepApplication : DaggerVectorApp(), IApplication {

    @Inject
    lateinit var matrixEventHandler: IMatrixEventHandler;
    @Inject
    lateinit var database: ClearKeepDatabase;
    @Inject
    lateinit var autoKeyBackup: IAutoKeyBackup;

    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var signatureRepository: SignatureRepository
    @Inject
    lateinit var keyBackupRepository: KeyBackupRepository
    @Inject
    lateinit var roomDao: AbstractRoomDao
    @Inject
    lateinit var appExecutors: AppExecutors

    private var session: MXSession? = null;

    private var currentTheme: Int = R.style.LightTheme;

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { throwable -> Log.d("RX Throw: ", throwable.message) }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent as AndroidInjector<out DaggerApplication>;
    }

    override fun setEventHandler() {
        session = Matrix.getInstance(this).defaultSession;
        Observable.timer(5, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    session?.let {
                        it.dataHandler.removeListener(matrixEventHandler.getMXEventListener(it));
                        it.dataHandler.addListener(matrixEventHandler.getMXEventListener(it))
                    }
                }

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
    private var boolStartingAutobackup = false;
    override fun startAutoKeyBackup(password: String?) {
//        if (boolStartingAutobackup)
//            return;
//        boolStartingAutobackup = true;
        session?.let { autoKeyBackup.startAutoKeyBackup(it.myUserId, password) }
    }
}