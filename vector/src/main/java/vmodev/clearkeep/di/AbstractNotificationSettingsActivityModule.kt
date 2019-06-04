package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.NotificationSettingsActivity
import vmodev.clearkeep.activities.interfaces.INotificationSettingsActivity
import vmodev.clearkeep.factories.viewmodels.NotificationSettingsViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.INotificationSettingsActivityViewModelFactory

@Module
@Suppress("unused")
abstract class AbstractNotificationSettingsActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeNotificationSettingActivity(): NotificationSettingsActivity;

    @Binds
    abstract fun bindNotificationSettingsActivity(activity: NotificationSettingsActivity): INotificationSettingsActivity;

    @Binds
    abstract fun bindNotificationSettingsActivityViewModelFactory(factory: NotificationSettingsViewModelFactory): INotificationSettingsActivityViewModelFactory;
}