package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.RolesPermissionActivity

@Module
@Suppress("unused")
abstract class AbstractRolesPermissionActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeRolesPermissionActivity(): RolesPermissionActivity;
}