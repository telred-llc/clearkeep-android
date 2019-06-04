package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.ViewUserProfileActivity

@Module
@Suppress("unused")
abstract class AbstractViewUserProfileActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeViewUserProfileActivity(): ViewUserProfileActivity;
}