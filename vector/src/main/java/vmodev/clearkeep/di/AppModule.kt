package vmodev.clearkeep.di

import android.arch.persistence.room.Room
import android.support.v7.util.DiffUtil
import dagger.Module
import dagger.Provides
import im.vector.BuildConfig
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomRecyclerViewAdapter
import vmodev.clearkeep.aes.AESCrypto
import vmodev.clearkeep.aes.interfaces.ICrypto
import vmodev.clearkeep.applications.ClearKeepApplication
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.databases.*
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.activitiesandfragments.DirectMessageFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.RoomMessageFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShowListRoomFragmentFactory
import vmodev.clearkeep.pbkdf2.PBKDF2GenerateKey
import vmodev.clearkeep.pbkdf2.interfaces.IGenerateKey
import vmodev.clearkeep.rests.ClearKeepApis
import vmodev.clearkeep.rests.IRetrofit
import vmodev.clearkeep.rests.RetrofitBuilder
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
                return p0.room?.id == p1.room?.id;
            }

            override fun areContentsTheSame(p0: vmodev.clearkeep.viewmodelobjects.RoomListUser, p1: vmodev.clearkeep.viewmodelobjects.RoomListUser): Boolean {
                return p0.room?.name == p1.room?.name && p0.room?.updatedDate == p1.room?.updatedDate && p0.room?.avatarUrl == p1.room?.avatarUrl
                        && p0.room?.notifyCount == p1.room?.notifyCount && p0.room?.type == p1.room?.type
                        && p0.room?.messageId == p1.room?.messageId && p0.room?.notificationState == p1.room?.notificationState
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

    @Provides
    @Singleton
    @Named(value = IRetrofit.BASE_URL_HOME_SERVER)
    fun provideRetrofit(): IRetrofit {
        return RetrofitBuilder(BuildConfig.HOME_SERVER);
    }

    @Provides
    @Singleton
    @Named(value = IRetrofit.BASE_URL_CLEAR_KEEP_SERVER)
    fun provideRetrofitClearKeep(): IRetrofit {
        return RetrofitBuilder(BuildConfig.CLEAR_KEEP_SERVER);
    }

    @Provides
    @Singleton
    @Named(value = IRetrofit.BASE_URL_HOME_SERVER)
    fun provideClearKeepApis(@Named(IRetrofit.BASE_URL_HOME_SERVER) retrofit: IRetrofit): ClearKeepApis {
        return retrofit.getRetrofit().create(ClearKeepApis::class.java);
    }

    @Provides
    @Singleton
    @Named(value = IRetrofit.BASE_URL_CLEAR_KEEP_SERVER)
    fun provideClearKeepApisClearKeep(@Named(IRetrofit.BASE_URL_CLEAR_KEEP_SERVER) retrofit: IRetrofit): ClearKeepApis {
        return retrofit.getRetrofit().create(ClearKeepApis::class.java);
    }

    @Provides
    @Singleton
    fun provideHashPassword(): IGenerateKey {
        return PBKDF2GenerateKey();
    }

    @Provides
    @Singleton
    fun provideCrypto(generateKey: IGenerateKey): ICrypto {
        return AESCrypto(generateKey);
    }
}