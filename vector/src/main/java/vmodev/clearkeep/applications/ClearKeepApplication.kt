package vmodev.clearkeep.applications

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import vmodev.clearkeep.di.AppInjector

class ClearKeepApplication : Application(), HasActivityInjector {
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>;

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this);
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}