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
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.repositories.interfaces.IRepository
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, MatrixSDKModule::class, AbstractMatrixSDKModule::class, AbstractDialogFragmentModules::class, AbstractRepositoryModule::class])
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
    fun provideUserDao(clearKeepDatabase: ClearKeepDatabase): AbstractUserDao {
        return clearKeepDatabase.userDao();
    }

    @Singleton
    @Provides
    fun provideRoomDao(clearKeepDatabase: ClearKeepDatabase): AbstractRoomDao {
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

    @Singleton
    @Provides
    fun provideSignature(clearKeepDatabase: ClearKeepDatabase): AbstractSignatureDao {
        return clearKeepDatabase.signatureDao();
    }

    @Singleton
    @Provides
    fun provideKeyBackup(clearKeepDatabase: ClearKeepDatabase): AbstractKeyBackupDao {
        return clearKeepDatabase.keyBackupDao();
    }

    @Singleton
    @Provides
    fun provideLocalSettings(clearKeepDatabase: ClearKeepDatabase): AbstractLocalSettingsDao {
        return clearKeepDatabase.localSettingsDao();
    }

    @Provides
    @Named(value = IListRoomRecyclerViewAdapter.ROOM)
    fun provideListRoomDirectMessageAdapter(appExecutors: AppExecutors): IListRoomRecyclerViewAdapter {
        return ListRoomRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<vmodev.clearkeep.viewmodelobjects.RoomListUser>() {
            override fun areItemsTheSame(p0: vmodev.clearkeep.viewmodelobjects.RoomListUser, p1: vmodev.clearkeep.viewmodelobjects.RoomListUser): Boolean {
                return p0.room?.get(0)?.id == p1.room?.get(0)?.id;
            }

            override fun areContentsTheSame(p0: vmodev.clearkeep.viewmodelobjects.RoomListUser, p1: vmodev.clearkeep.viewmodelobjects.RoomListUser): Boolean {
                var status = true;
                if (p0.users?.size != p1.users?.size) {
                    status = false;
                } else {
                    p0.users?.let { p00 ->
                        p1.users?.let { p11 ->
                            var index = 0;
                            while (index < p00.size) {
                                if (p00[index].status != p11[index].status) {
                                    status = false;
                                    index = p00.size;
                                }
                                index++;
                            }
                        } ?: run {
                            status = false;
                        }
                    } ?: run {
                        status = false;
                    }
                }
                return p0.room?.get(0)?.name == p1.room?.get(0)?.name && p0.room?.get(0)?.updatedDate == p1.room?.get(0)?.updatedDate && p0.room?.get(0)?.avatarUrl == p1.room?.get(0)?.avatarUrl
                        && p0.room?.get(0)?.notifyCount == p1.room?.get(0)?.notifyCount && p0.room?.get(0)?.type == p1.room?.get(0)?.type
                        && p0.room?.get(0)?.lastMessage == p1.room?.get(0)?.lastMessage && p0.room?.get(0)?.notificationState == p1.room?.get(0)?.notificationState
                        && status;
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