package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import javax.inject.Named

@Module
abstract class AbstractRoomActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeRoomActivity() : RoomActivity;

    @Binds
    @Named(IActivity.ROOM_ACTIVITY)
    abstract fun bindRoomActivity(activity : RoomActivity) : IActivity;
}