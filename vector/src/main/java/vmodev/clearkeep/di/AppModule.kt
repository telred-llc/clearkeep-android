package vmodev.clearkeep.di

import android.arch.persistence.room.Room
import android.support.v7.util.DiffUtil
import dagger.Module
import dagger.Provides
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomRecyclerViewAdapter
import vmodev.clearkeep.applications.ClearKeepApplication
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.databases.*
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.activitiesandfragments.DirectMessageFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.RoomMessageFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShowListRoomFragmentFactory
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, MatrixSDKModule::class, AbstractMatrixSDKModule::class, AbstractDialogFragmentModules::class])
class AppModule {

    @Provides
    @Singleton
    fun bindApplication(application: ClearKeepApplication): IApplication {
        return application;
    }

    @Singleton
    @Provides
    fun provideDB(application: IApplication): ClearKeepDatabase {
        return Room.databaseBuilder(application.getApplication(), ClearKeepDatabase::class.java, "clearkeep.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    fun provideUserDao(clearKeepDatabase: ClearKeepDatabase): UserDao {
        return clearKeepDatabase.userDao();
    }

    @Singleton
    @Provides
    fun provideRoomDao(clearKeepDatabase: ClearKeepDatabase): RoomDao {
        return clearKeepDatabase.roomDao();
    }

    @Singleton
    @Provides
    fun provideRoomUserDao(clearKeepDatabase: ClearKeepDatabase): AbstractRoomUserJoinDao {
        return clearKeepDatabase.roomUserJoinDao();
    }

    @Singleton
    @Provides
    fun provideDeviceSettingsDao(clearKeepDatabase: ClearKeepDatabase): AbstractDeviceSettingsDao {
        return clearKeepDatabase.deviceSettingsDao();
    }

    @Singleton
    @Provides
    fun provideBackupKeyPath(clearKeepDatabase: ClearKeepDatabase): AbstractBackupKeyPathDao {
        return clearKeepDatabase.backupKeyPathDao();
    }

    @Provides
    @Named(value = IListRoomRecyclerViewAdapter.ROOM)
    fun provideListRoomDirectMessageAdapter(appExecutors: AppExecutors): IListRoomRecyclerViewAdapter {
        return ListRoomRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<vmodev.clearkeep.viewmodelobjects.Room>() {
            override fun areItemsTheSame(p0: vmodev.clearkeep.viewmodelobjects.Room, p1: vmodev.clearkeep.viewmodelobjects.Room): Boolean {
                return p0.id == p1.id;
            }

            override fun areContentsTheSame(p0: vmodev.clearkeep.viewmodelobjects.Room, p1: vmodev.clearkeep.viewmodelobjects.Room): Boolean {
                return p0.name == p1.name && p0.updatedDate == p1.updatedDate && p0.avatarUrl == p1.avatarUrl
                        && p0.notifyCount == p1.notifyCount && p0.roomMemberStatus == p1.roomMemberStatus && p0.type == p1.type
                        && p0.lastMessage == p1.lastMessage;
            }
        })
    }

    @Singleton
    @Provides
    fun provideMessageDao(clearKeepDatabase: ClearKeepDatabase): AbstractMessageDao {
        return clearKeepDatabase.messageDao();
    }

    @Provides
    @Named(value = IShowListRoomFragmentFactory.DIRECT_MESSAGE_FRAGMENT_FACTORY)
    fun provideDirectMessageFragmentFactory(): IShowListRoomFragmentFactory {
        return DirectMessageFragmentFactory();
    }

    @Provides
    @Named(value = IShowListRoomFragmentFactory.ROOM_MESSAGE_FRAGMENT_FACTORY)
    fun provideRoomMessageFragmentFactory(): IShowListRoomFragmentFactory {
        return RoomMessageFragmentFactory();
    }
}