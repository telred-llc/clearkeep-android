package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.ProfileActivity

@Suppress("unused")
@Module
abstract class ProfileActivityModule {
    @ContributesAndroidInjector(modules = [])
    abstract fun contributeProfileActivity() : ProfileActivity;
}