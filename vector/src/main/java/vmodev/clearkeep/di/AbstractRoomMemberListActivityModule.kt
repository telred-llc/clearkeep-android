package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.RoomMemberListActivity

@Module
@Suppress("unused")
abstract class AbstractRoomMemberListActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeRoomMemberListActivity(): RoomMemberListActivity;
}