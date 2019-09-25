package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.RoomSettingsActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractRoomSettingsActivityModule {
    @ContributesAndroidInjector(modules = [AbstractRoomSettingsActivityFragmentBuilderModule::class, ActivityBindModule::class])
    abstract fun contributeRoomSettingsActivity(): RoomSettingsActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.ROOM_SETTINGS_ACTIVITY)
        abstract fun bindRoomSettingsActivity(activity: RoomSettingsActivity): IActivity;
    }
}