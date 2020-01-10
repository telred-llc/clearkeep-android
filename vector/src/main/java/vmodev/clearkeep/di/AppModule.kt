package vmodev.clearkeep.di

import android.text.TextUtils
import androidx.recyclerview.widget.DiffUtil
import dagger.Binds
import dagger.Module
import dagger.Provides
import im.vector.BuildConfig
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom
import vmodev.clearkeep.adapters.Interfaces.IListRoomDirectoryRecyclerViewAdapter
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomSearchDirectoryRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomSearchRecyclerViewAdapter
import vmodev.clearkeep.adapters.ShareFileRecyclerViewAdapter
import vmodev.clearkeep.aes.AESCrypto
import vmodev.clearkeep.aes.interfaces.ICrypto
import vmodev.clearkeep.applications.ClearKeepApplication
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.autokeybackups.AutoKeyBackup
import vmodev.clearkeep.autokeybackups.interfaces.IAutoKeyBackup
import vmodev.clearkeep.bindingadapters.BindingAdaptersImplement
import vmodev.clearkeep.bindingadapters.DataBindingComponentImplement
import vmodev.clearkeep.bindingadapters.interfaces.IDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.activitiesandfragments.DirectMessageFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.DirectMessageShareFileFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.RoomMessageFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.RoomShareFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShareFileFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShowListRoomFragmentFactory
import vmodev.clearkeep.pbkdf2.PBKDF2GenerateKey
import vmodev.clearkeep.pbkdf2.interfaces.IGenerateKey
import vmodev.clearkeep.rests.ClearKeepApis
import vmodev.clearkeep.rests.IRetrofit
import vmodev.clearkeep.rests.RetrofitBuilder
import vmodev.clearkeep.workermanager.UpdateDatabaseFromMatrixEvent
import vmodev.clearkeep.workermanager.interfaces.IUpdateDatabaseFromMatrixEvent
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, AbstractDatabaseModule::class, AbstractMatrixSDKModule::class, AbstractRepositoryModule::class])
@Suppress("unused")
abstract class AppModule {

    @Binds
    abstract fun bindApplication(application: ClearKeepApplication): IApplication

    @Binds
    @Named(value = IShowListRoomFragmentFactory.DIRECT_MESSAGE_FRAGMENT_FACTORY)
    abstract fun bindDirectMessageFragmentFactory(directMessageFragmentFactory: DirectMessageFragmentFactory): IShowListRoomFragmentFactory

    @Binds
    @Named(value = IShowListRoomFragmentFactory.ROOM_MESSAGE_FRAGMENT_FACTORY)
    abstract fun bindRoomMessageFragmentFactory(roomMessageFragmentFactory: RoomMessageFragmentFactory): IShowListRoomFragmentFactory

    @Binds
    @Singleton
    abstract fun bindHashPassword(pBKDF2GenerateKey: PBKDF2GenerateKey): IGenerateKey

    @Binds
    @Singleton
    abstract fun bindCrypto(aesCrypto: AESCrypto): ICrypto

    @Binds
    @Singleton
    abstract fun bindAutoKeyBackup(autoKeyBackup: AutoKeyBackup): IAutoKeyBackup

    @Binds
    abstract fun bindDataBindingComponent(dataBindingComponent: DataBindingComponentImplement): IDataBindingComponent


    @Binds
    @Named(value = IShareFileFragmentFactory.DIRECT_MESSAGE_SHARE_FILE_FRAGMENT_FACTORY)
    abstract fun bindDirectMessageShareFileFragmentFactory(directMessageShareFileFragmentFactory: DirectMessageShareFileFragmentFactory): IShareFileFragmentFactory

    @Binds
    @Named(value = IShareFileFragmentFactory.ROOM_MESSAGE_SHARE_FILE_FRAGMENT_FACTORY)
    abstract fun bindRoomMessageShareFileFragmentFactory(roomShareFileFragmentViewModelFactory: RoomShareFragmentFactory): IShareFileFragmentFactory


    @Binds
    @Singleton
    abstract fun bindUpdateDatabaseFromMatrixEvent(updateDatabaseFromMatrixEvent: UpdateDatabaseFromMatrixEvent): IUpdateDatabaseFromMatrixEvent

    @Module
    companion object {
        @Provides
        @JvmStatic
        @Named(value = IListRoomRecyclerViewAdapter.ROOM)
        fun provideListRoomDirectMessageAdapter(appExecutors: AppExecutors, dataBindingComponent: IDataBindingComponent): IListRoomRecyclerViewAdapter {
            return ListRoomRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<vmodev.clearkeep.viewmodelobjects.RoomListUser>() {
                override fun areItemsTheSame(p0: vmodev.clearkeep.viewmodelobjects.RoomListUser, p1: vmodev.clearkeep.viewmodelobjects.RoomListUser): Boolean {
                    return TextUtils.equals(p0.room?.id, p1.room?.id)
                }

                override fun areContentsTheSame(p0: vmodev.clearkeep.viewmodelobjects.RoomListUser, p1: vmodev.clearkeep.viewmodelobjects.RoomListUser): Boolean {
                    return p0.room?.name == p1.room?.name && p0.room?.avatarUrl == p1.room?.avatarUrl
                            && p0.room?.notifyCount == p1.room?.notifyCount && p0.room?.type == p1.room?.type
                            && p0.room?.messageId == p1.room?.messageId && p0.room?.notificationState == p1.room?.notificationState
                            && TextUtils.equals(p0.lastMessage?.id, p1.lastMessage?.id)
                            && p0.lastMessage?.createdAt == p1.lastMessage?.createdAt
                }
            }, dataBindingComponent = dataBindingComponent)

        }

        @Provides
        @JvmStatic
        @Named(value = IListRoomRecyclerViewAdapter.SEARCH_ROOM)
        fun provideListRoomSearchAdapter(appExecutors: AppExecutors, dataBindingComponent: IDataBindingComponent): IListRoomRecyclerViewAdapter {
            return ListRoomSearchRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<vmodev.clearkeep.viewmodelobjects.RoomListUser>() {
                override fun areItemsTheSame(p0: vmodev.clearkeep.viewmodelobjects.RoomListUser, p1: vmodev.clearkeep.viewmodelobjects.RoomListUser): Boolean {
                    return TextUtils.equals(p0.room?.id, p1.room?.id)
                }

                override fun areContentsTheSame(p0: vmodev.clearkeep.viewmodelobjects.RoomListUser, p1: vmodev.clearkeep.viewmodelobjects.RoomListUser): Boolean {
                    return p0.room?.name == p1.room?.name && p0.room?.avatarUrl == p1.room?.avatarUrl
                            && p0.room?.notifyCount == p1.room?.notifyCount && p0.room?.type == p1.room?.type
                            && p0.room?.messageId == p1.room?.messageId && p0.room?.notificationState == p1.room?.notificationState
                            && TextUtils.equals(p0.lastMessage?.id, p1.lastMessage?.id)

                            && p0.lastMessage?.createdAt == p1.lastMessage?.createdAt && p0.members?.size == p1.members?.size;
                }
            }, dataBindingComponent = dataBindingComponent)

        }

        @Provides
        @JvmStatic
        @Named(value = IListRoomDirectoryRecyclerViewAdapter.SEARCH_ROOMDIRECTORY)
        fun provideListRoomDirectorySearchAdapter(appExecutors: AppExecutors, dataBindingComponent: IDataBindingComponent): IListRoomDirectoryRecyclerViewAdapter {
            return ListRoomSearchDirectoryRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<PublicRoom>() {
                override fun areItemsTheSame(p0: PublicRoom, p1: PublicRoom): Boolean {
                    return TextUtils.equals(p0.roomId, p1.roomId)
                }

                override fun areContentsTheSame(p0: PublicRoom, p1: PublicRoom): Boolean {
                    return p0.name == p1.name && p0.avatarUrl == p1.avatarUrl
                            && p0.topic == p1.topic && p0.roomId == p1.roomId
                            && p0.guestCanJoin == p1.guestCanJoin
                }
            }, dataBindingComponent = dataBindingComponent)

        }

        @Provides
        @JvmStatic
        @Named(value = IListRoomRecyclerViewAdapter.SHARE_FILE)
        fun provideListShareFileAdapter(appExecutors: AppExecutors, dataBindingComponent: IDataBindingComponent): IListRoomRecyclerViewAdapter {
            return ShareFileRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<vmodev.clearkeep.viewmodelobjects.RoomListUser>() {
                override fun areItemsTheSame(p0: vmodev.clearkeep.viewmodelobjects.RoomListUser, p1: vmodev.clearkeep.viewmodelobjects.RoomListUser): Boolean {
                    return TextUtils.equals(p0.room?.id, p1.room?.id)
                }

                override fun areContentsTheSame(p0: vmodev.clearkeep.viewmodelobjects.RoomListUser, p1: vmodev.clearkeep.viewmodelobjects.RoomListUser): Boolean {
                    return p0.room?.name == p1.room?.name && p0.room?.avatarUrl == p1.room?.avatarUrl
                            && p0.room?.notifyCount == p1.room?.notifyCount && p0.room?.type == p1.room?.type
                            && p0.room?.messageId == p1.room?.messageId && p0.room?.notificationState == p1.room?.notificationState
                            && TextUtils.equals(p0.lastMessage?.id, p1.lastMessage?.id)
                            && p0.lastMessage?.createdAt == p1.lastMessage?.createdAt
                }
            }, dataBindingComponent = dataBindingComponent)
        }

        @Provides
        @Singleton
        @JvmStatic
        @Named(value = IRetrofit.BASE_URL_HOME_SERVER)
        fun provideRetrofit(): IRetrofit {
            return RetrofitBuilder(BuildConfig.HOME_SERVER)
        }

        @Provides
        @Singleton
        @JvmStatic
        @Named(value = IRetrofit.BASE_URL_CLEAR_KEEP_SERVER)
        fun provideRetrofitClearKeep(): IRetrofit {
            return RetrofitBuilder(BuildConfig.CLEAR_KEEP_SERVER)
        }

        @Provides
        @Singleton
        @JvmStatic
        @Named(value = IRetrofit.BASE_URL_HOME_SERVER)
        fun provideClearKeepApis(@Named(IRetrofit.BASE_URL_HOME_SERVER) retrofit: IRetrofit): ClearKeepApis {
            return retrofit.getRetrofit().create(ClearKeepApis::class.java)
        }

        @Provides
        @Singleton
        @JvmStatic
        @Named(value = IRetrofit.BASE_URL_CLEAR_KEEP_SERVER)
        fun provideClearKeepApisClearKeep(@Named(IRetrofit.BASE_URL_CLEAR_KEEP_SERVER) retrofit: IRetrofit): ClearKeepApis {
            return retrofit.getRetrofit().create(ClearKeepApis::class.java)
        }

        @Provides
        @JvmStatic
        fun provideBindingAdapter(): BindingAdaptersImplement {
            return BindingAdaptersImplement()
        }
    }
}