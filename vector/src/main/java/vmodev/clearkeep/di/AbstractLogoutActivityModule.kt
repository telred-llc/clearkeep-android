package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import im.vector.activity.LoggingOutActivity

@Module
@Suppress("unused")
abstract class AbstractLogoutActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeLogoutActivity(): LoggingOutActivity;
}