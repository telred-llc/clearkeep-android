package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.NotificationSettingsActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.INotificationSettingsActivity
import vmodev.clearkeep.factories.viewmodels.NotificationSettingsViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.INotificationSettingsActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractNotificationSettingsActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractNotificationSettingsActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeNotificationSettingActivity(): NotificationSettingsActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.NOTIFICATION_SETTINGS_ACTIVITY)
        abstract fun bindNotificationSettingsActivity(activity: NotificationSettingsActivity): IActivity;

        @Binds
        abstract fun bindNotificationSettingsActivityViewModelFactory(factory: NotificationSettingsViewModelFactory): IViewModelFactory<AbstractNotificationSettingsActivityViewModel>;
    }
}