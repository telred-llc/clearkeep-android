package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.InviteUsersToRoomActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.InviteUsersToRoomViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractInviteUsersToRoomActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class InviteUsersToRoomActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeInviteUsersToRoomActivity(): InviteUsersToRoomActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.INVITE_USERS_TO_ROOM_ACTIVITY)
        abstract fun bindInviteUsersToRoomActivity(activity: InviteUsersToRoomActivity): IActivity;

        @Binds
        abstract fun bindInviteUsersToRoomActivityViewModelFactory(factory: InviteUsersToRoomViewModelFactory): IViewModelFactory<AbstractInviteUsersToRoomActivityViewModel>;
    }
}