package vmodev.clearkeep.applications

import android.app.Activity
import android.app.Application
import android.content.DialogInterface
import android.util.Log
import androidx.appcompat.app.AlertDialog
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
import im.vector.util.openPlayStore
import io.fabric.sdk.android.Fabric
import io.reactivex.Observable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.Debug
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
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
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

    @Inject
    lateinit var matrixService: MatrixService

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

    override fun checkVersion(mActivity: Activity) {
        matrixService.getVersionApp().subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe({
            if (!it.data?.version.equals(BuildConfig.VERSION_NAME)) {
                val alertDialog: AlertDialog.Builder? = AlertDialog.Builder(mActivity)
                alertDialog?.setTitle(R.string.title_dialog_new_version_avaliable)
                alertDialog?.setMessage(R.string.message_dialog_update_version_application)
//                alertDialog?.setNegativeButton(R.string.title_button_cancel) { dialog: DialogInterface, which: Int ->
//                }
                alertDialog?.setPositiveButton(R.string.title_button_confirm) { dialog: DialogInterface, which: Int ->
                    openPlayStore(mActivity, packageName)
                }
                alertDialog?.setCancelable(false)
                alertDialog?.show()
            }
        }, {
            Log.e("Tag", "--- Error: ${it.message}")

        })
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

    fun saveStore() {
        session?.crypto?.let { crypto ->
            userDao.findAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .doFinally {
                    }.subscribe(object : SingleObserver<List<User>> {
                        override fun onSuccess(lstUser: List<User>) {
                            lstUser.forEach { user ->
                                crypto.getUserDevices(user.id).forEach { mxDeviceInfo ->
                                    crypto.setDeviceVerification(MXDeviceInfo.DEVICE_VERIFICATION_VERIFIED, mxDeviceInfo.deviceId, user.id, object : ApiCallback<Void> {
                                        override fun onSuccess(info: Void?) {
                                        }

                                        override fun onUnexpectedError(e: Exception?) {
                                            Debug.e("--- Error: ${e?.message}")
                                        }

                                        override fun onMatrixError(e: MatrixError?) {
                                            Debug.e("--- Error: ${e?.message}")
                                        }

                                        override fun onNetworkError(e: Exception?) {
                                            Debug.e("--- Error: ${e?.message}")
                                        }
                                    })

                                }
                            }
                        }

                        override fun onSubscribe(d: Disposable) {
                            Debug.e("--- onSubscribe")
                        }

                        override fun onError(e: Throwable) {
                            Log.d("DeviceId", e.message)
                        }
                    })
        }
    }

    override fun startAutoKeyBackup(password: String?, action: IApplication.IAction?) {
        session?.let { mxSession ->
            autoKeyBackup.startAutoKeyBackup(mxSession.myUserId, password, action!!)
        }
    }

    override fun getUserId(): String {
        session?.let { return it.myUserId } ?: run { return "" }
    }

}