package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.RoomMemberListActivity
import vmodev.clearkeep.activities.RoomfilesListActivity
import vmodev.clearkeep.activities.interfaces.IRoomFileListActivity
import vmodev.clearkeep.factories.viewmodels.RoomFileListActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IRoomFileListActivityViewModelFactory
import javax.inject.Inject

@Module
@Suppress("unused")
abstract class AbstractRoomFilesListActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeRoomFilesListActivity(): RoomfilesListActivity;

    @Binds
    abstract fun bindRoomFileListActivity(activity: RoomfilesListActivity): IRoomFileListActivity;

    @Binds
    abstract fun bindRoomFileListActivityViewModelFactory(factory: RoomFileListActivityViewModelFactory): IRoomFileListActivityViewModelFactory;
}