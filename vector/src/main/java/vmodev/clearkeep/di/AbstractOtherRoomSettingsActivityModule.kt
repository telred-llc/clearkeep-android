package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.OtherRoomSettingsActivity
import vmodev.clearkeep.activities.interfaces.IOtherRoomSettingsActivity

@Module
@Suppress("unused")
abstract class AbstractOtherRoomSettingsActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeOtherRoomSettingsActivity(): OtherRoomSettingsActivity;

    @Binds
    abstract fun bindOtherRoomSettingsActivity(activity: OtherRoomSettingsActivity): IOtherRoomSettingsActivity;
}