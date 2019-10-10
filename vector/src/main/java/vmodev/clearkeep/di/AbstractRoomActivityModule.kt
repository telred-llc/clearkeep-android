package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import im.vector.activity.JitsiCallActivity
import vmodev.clearkeep.activities.RoomActivity

@Module
abstract class AbstractRoomActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeRoomActivity(): RoomActivity;
    @ContributesAndroidInjector
    abstract fun contributeJitsiCallActivity(): JitsiCallActivity;
}