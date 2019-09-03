package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.RoomActivity

@Module
abstract class AbstractRoomActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeRoomActivity(): RoomActivity;
}