package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.ProfileSettingsActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IProfileSettingsActivity
import vmodev.clearkeep.factories.viewmodels.ProfileSettingsActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IProfileSettingsActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractProfileSettingsActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractProfileSettingsActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeProfileSettingsActivity(): ProfileSettingsActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.PROFILE_SETTINGS_ACTIVITY)
        abstract fun bindProfileSettingsActivity(activity: ProfileSettingsActivity): IActivity;

        @Binds
        abstract fun bindProfileSettingsActivityViewModelFactory(factory: ProfileSettingsActivityViewModelFactory): IViewModelFactory<AbstractProfileSettingsActivityViewModel>;
    }

}