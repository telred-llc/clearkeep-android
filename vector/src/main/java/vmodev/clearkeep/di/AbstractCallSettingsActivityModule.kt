package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.CallSettingsActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.CallSettingsActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractCallSettingActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractCallSettingsActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeCallSettingsActivity(): CallSettingsActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.CALL_SETTINGS_ACTIVITY)
        abstract fun bindCallSettingsActivity(activity: CallSettingsActivity): IActivity;

        @Binds
        abstract fun bindCallSettingsActivityViewModelFactory(factory: CallSettingsActivityViewModelFactory): IViewModelFactory<AbstractCallSettingActivityViewModel>;
    }
}