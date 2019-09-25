package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.RoomMemberListActivity
import vmodev.clearkeep.activities.RoomfilesListActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IRoomFileListActivity
import vmodev.clearkeep.factories.viewmodels.RoomFileListActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IRoomFileListActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomFileListActivityViewModel
import javax.inject.Inject
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractRoomFilesListActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeRoomFilesListActivity(): RoomfilesListActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.ROOM_FILES_LIST_ACTIVITY)
        abstract fun bindRoomFileListActivity(activity: RoomfilesListActivity): IActivity;

        @Binds
        abstract fun bindRoomFileListActivityViewModelFactory(factory: RoomFileListActivityViewModelFactory): IViewModelFactory<AbstractRoomFileListActivityViewModel>;
    }
}