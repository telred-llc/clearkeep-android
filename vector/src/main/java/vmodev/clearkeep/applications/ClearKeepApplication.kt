package vmodev.clearkeep.applications

import android.util.Log
import dagger.android.AndroidInjector
import im.vector.Matrix
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.databases.ClearKeepDatabase
import vmodev.clearkeep.di.DaggerAppComponent
import vmodev.clearkeep.matrixsdk.interfaces.IMatrixEventHandler
import javax.inject.Inject
import io.reactivex.plugins.RxJavaPlugins



class ClearKeepApplication : DaggerVectorApp() {

    @Inject
    lateinit var matrixEventHandler: IMatrixEventHandler;
    @Inject
    lateinit var database: ClearKeepDatabase;

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { throwable -> Log.d("RX Throw: ", throwable.message) }
//        val mxSession = Matrix.getInstance(this).defaultSession;
//        mxSession?.dataHandler?.addListener(matrixEventHandler.getMXEventListener(mxSession))
        Observable.create<Unit> { emitter ->
            run {
                val unit = database.clearAllTables();
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

    fun setEventHandler(){
        val mxSession = Matrix.getInstance(this).defaultSession;
        mxSession!!.dataHandler!!.addListener(matrixEventHandler.getMXEventListener(mxSession))
    }
}