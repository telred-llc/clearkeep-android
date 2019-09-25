package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.NewRoomActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractNewRoomActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class, AbstractNewRoomActivityBuilderFragmentBuilderModule::class])
    abstract fun contributeNewRoomActivity(): NewRoomActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.NEW_ROOM_ACTIVITY)
        abstract fun bindNewRoomActivity(activity: NewRoomActivity): IActivity;
    }
}