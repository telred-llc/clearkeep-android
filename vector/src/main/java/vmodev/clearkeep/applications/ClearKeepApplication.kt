package vmodev.clearkeep.applications

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import im.vector.Matrix
import org.matrix.androidsdk.MXSession
import vmodev.clearkeep.di.AppInjector
import vmodev.clearkeep.di.DaggerAppComponent
import vmodev.clearkeep.matrixsdk.IMatrixEventHandler
import javax.inject.Inject

class ClearKeepApplication : DaggerVectorApp() {

    @Inject
    lateinit var matrixEventHandler: IMatrixEventHandler;

    override fun onCreate() {
        super.onCreate()
        val mxSession = Matrix.getInstance(this).defaultSession;
        mxSession?.dataHandler?.addListener(matrixEventHandler.getMXEventListener(mxSession))
    }

    override fun applicationInjector(): AndroidInjector<out DaggerVectorApp> {
        val appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }
}