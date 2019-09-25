package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.OtherRoomSettingsActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IOtherRoomSettingsActivity
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractOtherRoomSettingsActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeOtherRoomSettingsActivity(): OtherRoomSettingsActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.OTHER_ROOM_SETTINGS_ACTIVITY)
        abstract fun bindOtherRoomSettingsActivity(activity: OtherRoomSettingsActivity): IActivity;
    }
}