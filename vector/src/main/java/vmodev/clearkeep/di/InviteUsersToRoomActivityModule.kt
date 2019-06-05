package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.InviteUsersToRoomActivity

@Module
abstract class InviteUsersToRoomActivityModule{
    @ContributesAndroidInjector(modules = [])
    abstract fun contributeInviteUsersToRoomActivity() : InviteUsersToRoomActivity;
}