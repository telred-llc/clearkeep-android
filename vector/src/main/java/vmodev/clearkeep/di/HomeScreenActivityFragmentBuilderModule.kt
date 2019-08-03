package vmodev.clearkeep.di

import android.support.v7.util.DiffUtil
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomContactRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomRecyclerViewAdapter
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.activitiesandfragments.*
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShowListRoomFragmentFactory
import vmodev.clearkeep.factories.viewmodels.*
import vmodev.clearkeep.factories.viewmodels.interfaces.*
import vmodev.clearkeep.fragments.*
import vmodev.clearkeep.fragments.Interfaces.*
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import javax.inject.Named

@Suppress("unused")
@Module
abstract class HomeScreenActivityFragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeScreenFragment(): HomeScreenFragment;

    @ContributesAndroidInjector
    abstract fun contributeFavouriteFragment(): FavouritesFragment;

    @ContributesAndroidInjector
    abstract fun contributeContactFragment(): ContactsFragment;

    @ContributesAndroidInjector
    abstract fun contributeListRoomFragment(): ListRoomFragment;

    @Binds
    abstract fun bindHomeScreenFragment(fragment: HomeScreenFragment): IHomeScreenFragment;

    @Binds
    abstract fun bindFavouritesFragment(fragment: FavouritesFragment): IFavouritesFragment;

    @Binds
    abstract fun bindContactFragment(fragment: ContactsFragment): IContactFragment;

    @Binds
    abstract fun bindListRoomFragment(fragment: ListRoomFragment): IListRoomFragment;

    @Binds
    abstract fun bindHomeScreenFragmentViewModelFactory(factory: HomeScreenFragmentViewModelFactory): IHomeScreenFragmentViewModelFactory;

    @Binds
    abstract fun bindFavouritesFragmentViewModelFactory(factory: FavouritesFragmentViewModelFactory): IFavouritesFragmentViewModelFactory;

    @Binds
    abstract fun bindContactFragmentViewModelFactory(factory: ContactFragmentViewModelFactory): IContactFragmentViewModelFactory;

    @Binds
    abstract fun bindListRoomFragmentViewModelFactory(factory: ListRoomFragmentViewModelFactory): IListRoomFragmentViewModelFactory;

    @Module
    companion object {
//        @Provides
//        @JvmStatic
//        @Named(value = IListRoomRecyclerViewAdapter.ROOM)
//        fun provideListRoomDirectMessageAdapter(appExecutors: AppExecutors): IListRoomRecyclerViewAdapter {
//            return ListRoomRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<Room>() {
//                override fun areItemsTheSame(p0: Room, p1: Room): Boolean {
//                    return p0.id == p1.id;
//                }
//
//                override fun areContentsTheSame(p0: Room, p1: Room): Boolean {
//                    return p0.name == p1.name && p0.updatedDate == p1.updatedDate && p0.avatarUrl == p1.avatarUrl
//                            && p0.notifyCount == p1.notifyCount && p0.roomMemberStatus == p1.roomMemberStatus;
//                }
//            })
//        }

        @Provides
        @JvmStatic
        @Named(value = IListRoomRecyclerViewAdapter.ROOM_CONTACT)
        fun provideListRoomContactAdapter(appExecutors: AppExecutors): IListRoomRecyclerViewAdapter {
            return ListRoomContactRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<RoomListUser>() {
                override fun areItemsTheSame(p0: RoomListUser, p1: RoomListUser): Boolean {
                    return p0.room?.get(0)?.id == p1.room?.get(0)?.id;
                }

                override fun areContentsTheSame(p0: RoomListUser, p1: RoomListUser): Boolean {
                    return p0.room?.get(0)?.name == p1.room?.get(0)?.name && p0.room?.get(0)?.updatedDate == p1.room?.get(0)?.updatedDate && p0.room?.get(0)?.avatarUrl == p1.room?.get(0)?.avatarUrl
                            && p0.room?.get(0)?.notifyCount == p1.room?.get(0)?.notifyCount;
                }
            })
        }


        @Provides
        @JvmStatic
        @Named(value = IFragmentFactory.HOME_SCREEN_FRAGMENT)
        fun provideHomeScreenFragmentFactory(): IFragmentFactory {
            return HomeScreenFragmentFactory();
        }

        @Provides
        @JvmStatic
        @Named(value = IFragmentFactory.FAVOURITES_FRAGMENT)
        fun provideFavouritesFragmentFactory(application: IApplication): IFragmentFactory {
            return FavouritesFragmentFactory(application);
        }

        @Provides
        @JvmStatic
        @Named(value = IFragmentFactory.CONTACTS_FRAGMENT)
        fun provideContactsFragmentFactory(application: IApplication): IFragmentFactory {
            return ContactsFragmentFactory(application);
        }

        @Provides
        @JvmStatic
        @Named(value = IFragmentFactory.LIST_ROOM_FRAGMENT)
        fun provideListRoomFragmentFactory(application: IApplication): IFragmentFactory {
            return ListRoomFragmentFactory(application);
        }
    }
}