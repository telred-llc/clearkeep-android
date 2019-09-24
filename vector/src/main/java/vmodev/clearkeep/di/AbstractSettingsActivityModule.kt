package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.SettingsActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractSettingsActivityModule {
    @ContributesAndroidInjector(modules = [ActivitySettingsBindModule::class, AbstractSettingsActivityFragmentBuilderModule::class])
    abstract fun contributeSettingsActivity() : SettingsActivity;

    @Module
    abstract class ActivitySettingsBindModule{
        @Binds
        @Named(IActivity.SETTINGS_ACTIVITY)
        abstract fun bindASettingsActivity(activity : SettingsActivity) : IActivity;
    }
}