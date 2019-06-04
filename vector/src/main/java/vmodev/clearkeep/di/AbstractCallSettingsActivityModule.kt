package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.CallSettingsActivity
import vmodev.clearkeep.activities.interfaces.ICallSettingsActivity
import vmodev.clearkeep.factories.viewmodels.CallSettingsActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.ICallSettingsActivityViewModelFactory

@Module
abstract class AbstractCallSettingsActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeCallSettingsActivity(): CallSettingsActivity;

    @Binds
    abstract fun bindCallSettingsActivity(activity: CallSettingsActivity): ICallSettingsActivity;

    @Binds
    abstract fun bindCallSettingsActivityViewModelFactory(factory: CallSettingsActivityViewModelFactory): ICallSettingsActivityViewModelFactory;
}