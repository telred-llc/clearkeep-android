package vmodev.clearkeep.applications

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import vmodev.clearkeep.di.AppInjector
import vmodev.clearkeep.di.DaggerAppComponent

class ClearKeepApplication : DaggerVectorApp() {
    override fun applicationInjector(): AndroidInjector<out DaggerVectorApp> {
        val appComponent: AndroidInjector<DaggerVectorApp> = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }
}