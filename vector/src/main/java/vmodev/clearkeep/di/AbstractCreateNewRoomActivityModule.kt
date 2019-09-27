package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.CreateNewRoomActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.fragments.CreateNewRoomFragment
import vmodev.clearkeep.fragments.InviteUsersToRoomFragment
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractCreateNewRoomActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class, ActivityFragmentBuilderModule::class])
    abstract fun contributeCreateNewRoomActivity() : CreateNewRoomActivity;

    @Module
    abstract class ActivityBindModule{
        @Binds
        @Named(IActivity.CREATE_NEW_ROOM_ACTIVITY)
        abstract fun bindCreateNewRoomActivity(activity : CreateNewRoomActivity): IActivity;
    }

    @Module
    abstract class ActivityFragmentBuilderModule{
        @ContributesAndroidInjector(modules = [AbstractNewRoomActivityBuilderFragmentBuilderModule.CreateNewRoomFragmentBindModule::class])
        abstract fun contributeCreateNewRoomFragment() : CreateNewRoomFragment;
        @ContributesAndroidInjector(modules = [AbstractNewRoomActivityBuilderFragmentBuilderModule.InviteUsersToRoomFragmentBindModule::class])
        abstract fun contributeInviteUsersToRoomFragment() : InviteUsersToRoomFragment;
    }
}