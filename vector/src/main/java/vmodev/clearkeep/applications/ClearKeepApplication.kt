package vmodev.clearkeep.applications

import android.app.Activity
import android.app.Application
import android.util.Log
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import im.vector.Matrix
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.databases.ClearKeepDatabase
import vmodev.clearkeep.di.AppInjector
import vmodev.clearkeep.di.DaggerAppComponent
import vmodev.clearkeep.matrixsdk.IMatrixEventHandler
import javax.inject.Inject
import io.reactivex.plugins.RxJavaPlugins



class ClearKeepApplication : DaggerVectorApp() {

    @Inject
    lateinit var matrixEventHandler: IMatrixEventHandler;
    @Inject
    lateinit var dataase: ClearKeepDatabase;

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { throwable -> Log.d("RX Throw: ", throwable.message) }
//        val mxSession = Matrix.getInstance(this).defaultSession;
//        mxSession?.dataHandler?.addListener(matrixEventHandler.getMXEventListener(mxSession))
        Observable.create<Unit> { emitter ->
            run {
                val unit = dataase.clearAllTables();
                if (unit != null) {
                    emitter.onNext(unit);
                    emitter.onComplete();
                } else {
                    emitter.onError(Throwable(message = "Can not delete database"))
                    emitter.onComplete();
                }
            }
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe { b ->
            Log.d("Delete DB Success", b.toString())
        };
    }

    override fun applicationInjector(): AndroidInjector<out DaggerVectorApp> {
        val appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }

    public fun setEventHandler(){
        val mxSession = Matrix.getInstance(this).defaultSession;
        mxSession!!.dataHandler!!.addListener(matrixEventHandler.getMXEventListener(mxSession))
    }
}