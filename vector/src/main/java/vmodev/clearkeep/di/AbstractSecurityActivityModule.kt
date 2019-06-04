package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.SecurityActivity

@Module
@Suppress("unused")
abstract class AbstractSecurityActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeSecurityActivity(): SecurityActivity;
}