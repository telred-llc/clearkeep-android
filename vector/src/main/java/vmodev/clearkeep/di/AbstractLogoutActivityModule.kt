package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import im.vector.activity.LoggingOutActivity

@Module
abstract class AbstractLogoutActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeLogoutActivity(): LoggingOutActivity;
}