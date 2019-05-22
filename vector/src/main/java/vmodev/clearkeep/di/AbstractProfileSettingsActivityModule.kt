package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.ProfileSettingsActivity
import vmodev.clearkeep.activities.interfaces.IProfileSettingsActivity
import vmodev.clearkeep.factories.viewmodels.ProfileSettingsActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IProfileSettingsActivityViewModelFactory

@Module
@Suppress("unused")
abstract class AbstractProfileSettingsActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeProfileSettingsActivity(): ProfileSettingsActivity;

    @Binds
    abstract fun bindProfileSettingsActivity(activity: ProfileSettingsActivity): IProfileSettingsActivity;

    @Binds
    abstract fun bindProfileSettingsActivityViewModelFactory(factory: ProfileSettingsActivityViewModelFactory): IProfileSettingsActivityViewModelFactory;
}