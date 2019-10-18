package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.RoomDetailActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractRoomDetailActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeRoomDetailActivity(): RoomDetailActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.ROOM_DETAIL_ACTIVITY)
        abstract fun bindRoomDetailActivity(activity : RoomDetailActivity): IActivity;
    }
}