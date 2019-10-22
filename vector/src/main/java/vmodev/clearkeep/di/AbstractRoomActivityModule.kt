package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import im.vector.activity.JitsiCallActivity
import vmodev.clearkeep.activities.RoomActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.RoomActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomActivityViewModel
import javax.inject.Named

@Suppress("unused")
@Module
abstract class AbstractRoomActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeJitsiCallActivity(): JitsiCallActivity;

    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeRoomActivity(): RoomActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.ROOM_ACTIVITY)
        abstract fun bindIHomeScreenActivity(activity: RoomActivity): IActivity;

        @Binds
        abstract fun bindHomeScreenViewModelFactory(factory: RoomActivityViewModelFactory): IViewModelFactory<AbstractRoomActivityViewModel>;
    }
}