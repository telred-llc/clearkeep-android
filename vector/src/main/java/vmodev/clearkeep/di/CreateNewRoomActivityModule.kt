package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.CreateNewRoomActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IViewUserProfileActivity
import vmodev.clearkeep.factories.viewmodels.CreateNewRoomActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractCreateNewRoomActivityViewModel
import javax.inject.Named

@Module
abstract class CreateNewRoomActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeCreateNewRoomActivity(): CreateNewRoomActivity;

    @Binds
    @Named(IActivity.CREATE_NEW_ROOM_ACTIVITY)
    abstract fun bindCreateNewRoomActivity(activity: CreateNewRoomActivity): IActivity;

    @Binds
    abstract fun bindCreateNewRoomActivityViewModelFactory(factory: CreateNewRoomActivityViewModelFactory): IViewModelFactory<AbstractCreateNewRoomActivityViewModel>;
}