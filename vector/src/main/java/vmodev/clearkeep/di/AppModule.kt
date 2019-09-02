package vmodev.clearkeep.di

import android.support.v7.util.DiffUtil
import dagger.Binds
import dagger.Module
import dagger.Provides
import im.vector.BuildConfig
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomRecyclerViewAdapter
import vmodev.clearkeep.aes.AESCrypto
import vmodev.clearkeep.aes.interfaces.ICrypto
import vmodev.clearkeep.applications.ClearKeepApplication
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.autokeybackups.AutoKeyBackup
import vmodev.clearkeep.autokeybackups.interfaces.IAutoKeyBackup
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

@Module(includes = [ViewModelModule::class, AbstractDatabaseModule::class, AbstractMatrixSDKModule::class, AbstractDialogFragmentModules::class, AbstractRepositoryModule::class])
@Suppress("unused")
abstract class AppModule {

    @Binds
    abstract fun bindApplication(application: ClearKeepApplication): IApplication;

    @Binds
    @Named(value = IShowListRoomFragmentFactory.DIRECT_MESSAGE_FRAGMENT_FACTORY)
    abstract fun bindDirectMessageFragmentFactory(directMessageFragmentFactory: DirectMessageFragmentFactory): IShowListRoomFragmentFactory;

    @Binds
    @Named(value = IShowListRoomFragmentFactory.ROOM_MESSAGE_FRAGMENT_FACTORY)
    abstract fun bindRoomMessageFragmentFactory(roomMessageFragmentFactory: RoomMessageFragmentFactory): IShowListRoomFragmentFactory;

    @Binds
    @Singleton
    abstract fun bindHashPassword(pBKDF2GenerateKey: PBKDF2GenerateKey): IGenerateKey;

    @Binds
    @Singleton
    abstract fun bindCrypto(aesCrypto: AESCrypto): ICrypto;

    @Binds
    @Singleton
    abstract fun bindAutoKeyBackup(autoKeyBackup: AutoKeyBackup): IAutoKeyBackup;

    @Module
    companion object {
        @Provides
        @JvmStatic
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


        @Provides
        @Singleton
        @JvmStatic
        @Named(value = IRetrofit.BASE_URL_HOME_SERVER)
        fun provideRetrofit(): IRetrofit {
            return RetrofitBuilder(BuildConfig.HOME_SERVER);
        }

        @Provides
        @Singleton
        @JvmStatic
        @Named(value = IRetrofit.BASE_URL_CLEAR_KEEP_SERVER)
        fun provideRetrofitClearKeep(): IRetrofit {
            return RetrofitBuilder(BuildConfig.CLEAR_KEEP_SERVER);
        }

        @Provides
        @Singleton
        @JvmStatic
        @Named(value = IRetrofit.BASE_URL_HOME_SERVER)
        fun provideClearKeepApis(@Named(IRetrofit.BASE_URL_HOME_SERVER) retrofit: IRetrofit): ClearKeepApis {
            return retrofit.getRetrofit().create(ClearKeepApis::class.java);
        }

        @Provides
        @Singleton
        @JvmStatic
        @Named(value = IRetrofit.BASE_URL_CLEAR_KEEP_SERVER)
        fun provideClearKeepApisClearKeep(@Named(IRetrofit.BASE_URL_CLEAR_KEEP_SERVER) retrofit: IRetrofit): ClearKeepApis {
            return retrofit.getRetrofit().create(ClearKeepApis::class.java);
        }
    }
}