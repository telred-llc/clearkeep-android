package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.OtherRoomSettingsAdvancedActivity

@Module
@Suppress("unused")
abstract class AbstractOtherRoomSettingsAdvancedActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeOtherRoomSettingsAdvancedActivity(): OtherRoomSettingsAdvancedActivity;
}