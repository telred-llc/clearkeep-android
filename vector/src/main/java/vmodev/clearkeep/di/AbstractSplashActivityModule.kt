package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.SplashActivity
import vmodev.clearkeep.activities.interfaces.ISplashActivity
import vmodev.clearkeep.factories.viewmodels.SplashActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.ISplashActivityViewModelFactory

@Module
@Suppress("unused")
abstract class AbstractSplashActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeSplashActivity(): SplashActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        abstract fun bindSplashActivity(activity: SplashActivity): ISplashActivity;

        @Binds
        abstract fun bindSplashActivityViewModelFactory(factory: SplashActivityViewModelFactory): ISplashActivityViewModelFactory;
    }
}