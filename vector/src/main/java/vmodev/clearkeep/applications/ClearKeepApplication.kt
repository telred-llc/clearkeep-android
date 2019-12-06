package vmodev.clearkeep.applications

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.Configuration
import androidx.work.WorkManager
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import im.vector.BuildConfig
import im.vector.Matrix
import im.vector.R
import io.fabric.sdk.android.Fabric
import io.reactivex.Observable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.crypto.data.MXDeviceInfo
import vmodev.clearkeep.autokeybackups.interfaces.IAutoKeyBackup
import vmodev.clearkeep.databases.AbstractRoomDao
import vmodev.clearkeep.databases.AbstractUserDao
import vmodev.clearkeep.databases.ClearKeepDatabase
import vmodev.clearkeep.di.AppComponent
import vmodev.clearkeep.di.DaggerAppComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.matrixsdk.interfaces.IMatrixEventHandler
import vmodev.clearkeep.repositories.KeyBackupRepository
import vmodev.clearkeep.repositories.SignatureRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.User
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ClearKeepApplication : DaggerVectorApp(), IApplication {

    @Inject
    lateinit var matrixEventHandler: IMatrixEventHandler
    @Inject
    lateinit var database: ClearKeepDatabase
    @Inject
    lateinit var autoKeyBackup: IAutoKeyBackup

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
    @Inject
    lateinit var userDao: AbstractUserDao

    private var session: MXSession? = null

    private var currentTheme: Int = R.style.LightTheme

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        val factory = appComponent.workerFactory()
        WorkManager.initialize(this, Configuration.Builder().setWorkerFactory(factory).build())
        super.onCreate()
        RxJavaPlugins.setErrorHandler { throwable -> Log.d("RX Throw: ", throwable.message) }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        val crashlytics = Crashlytics.Builder().core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build()
        Fabric.with(this, crashlytics)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {

        return appComponent as AndroidInjector<out DaggerApplication>
    }

    override fun setEventHandler() {
        session = Matrix.getInstance(this).defaultSession
        Observable.timer(5, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    session?.let {
                        it.dataHandler.removeListener(matrixEventHandler.getMXEventListener(it))
                        it.dataHandler.addListener(matrixEventHandler.getMXEventListener(it))
                    }
                }

    }

    override fun removeEventHandler() {
        session?.let { it.dataHandler.removeListener(matrixEventHandler.getMXEventListener(it)) }
    }

    override fun getCurrentTheme(): Int {
        return currentTheme
    }

    override fun setCurrentTheme(theme: Int) {
        currentTheme = theme
    }

    override fun getApplication(): Application {
        return this
    }

    override fun startAutoKeyBackup(password: String?) {
        session?.let { autoKeyBackup.startAutoKeyBackup(it.myUserId, password) }
        session?.let { s ->
            s.crypto?.let { crypto ->
                userDao.findAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : SingleObserver<List<User>> {
                    override fun onSuccess(t: List<User>) {
                        t.forEach { u ->
                            crypto.getUserDevices(u.id).forEach {
                                crypto.setDeviceVerification(MXDeviceInfo.DEVICE_VERIFICATION_VERIFIED, it.deviceId, u.id, object : ApiCallback<Void> {
                                    override fun onSuccess(info: Void?) {

                                    }

                                    override fun onUnexpectedError(e: Exception?) {

                                    }

                                    override fun onMatrixError(e: MatrixError?) {

                                    }

                                    override fun onNetworkError(e: Exception?) {

                                    }
                                })

                            }
                        }
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        Log.d("DeviceId", e.message)
                    }
                })
            }
        }
    }

    override fun getUserId(): String {
        session?.let { return it.myUserId } ?: run { return "" }
    }
}