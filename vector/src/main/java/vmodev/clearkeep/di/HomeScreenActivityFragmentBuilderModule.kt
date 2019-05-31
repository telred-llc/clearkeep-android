package vmodev.clearkeep.di

import android.support.v7.util.DiffUtil
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomContactRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.activitiesandfragments.*
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShowListRoomFragmentFactory
import vmodev.clearkeep.factories.viewmodels.*
import vmodev.clearkeep.factories.viewmodels.interfaces.*
import vmodev.clearkeep.fragments.*
import vmodev.clearkeep.fragments.Interfaces.*
import vmodev.clearkeep.viewmodelobjects.Room
import javax.inject.Named

@Suppress("unused")
@Module
abstract class HomeScreenActivityFragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeScreenFragment(): HomeScreenFragment;

    @ContributesAndroidInjector
    abstract fun contributeRoomFragment(): RoomFragment;

    @ContributesAndroidInjector
    abstract fun contributeDirectMessageFragment(): DirectMessageFragment;

    @ContributesAndroidInjector
    abstract fun contributeFavouriteFragment(): FavouritesFragment;

    @ContributesAndroidInjector
    abstract fun contributeContactFragment(): ContactsFragment;

    @Binds
    abstract fun bindDirectMessageFragment(fragment: DirectMessageFragment): IDriectMessageFragment;

    @Binds
    abstract fun bindHomeScreenFragment(fragment: HomeScreenFragment): IHomeScreenFragment;

    @Binds
    abstract fun bindRoomFragment(fragment: RoomFragment): IRoomFragment;

    @Binds
    abstract fun bindFavouritesFragment(fragment: FavouritesFragment): IFavouritesFragment;

    @Binds
    abstract fun bindContactFragment(fragment: ContactsFragment): IContactFragment;

    @Binds
    abstract fun bindDirectMessageFragmentViewModelFactory(factory: DirectMessageFragmentViewModelFactory): IDirectMessageFragmentViewModelFactory;

    @Binds
    abstract fun bindHomeScreenFragmentViewModelFactory(factory: HomeScreenFragmentViewModelFactory): IHomeScreenFragmentViewModelFactory;

    @Binds
    abstract fun bindRoomFragmentViewModelFactory(factory: RoomFragmentViewModelFactory): IRoomFragmentViewModelFactory;

    @Binds
    abstract fun bindFavouritesFragmentViewModelFactory(factory: FavouritesFragmentViewModelFactory): IFavouritesFragmentViewModelFactory;

    @Binds
    abstract fun bindContactFragmentViewModelFactory(factory: ContactFragmentViewModelFactory): IContactFragmentViewModelFactory;

    @Module
    companion object {
        @Provides
        @JvmStatic
        @Named(value = IListRoomRecyclerViewAdapter.ROOM)
        fun provideListRoomDirectMessageAdapter(appExecutors: AppExecutors): IListRoomRecyclerViewAdapter {
            return ListRoomRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<Room>() {
                override fun areItemsTheSame(p0: Room, p1: Room): Boolean {
                    return p0.id == p1.id;
                }

                override fun areContentsTheSame(p0: Room, p1: Room): Boolean {
                    return p0.name == p1.name && p0.updatedDate == p1.updatedDate && p0.avatarUrl == p1.avatarUrl
                            && p0.notifyCount == p1.notifyCount && p0.roomMemberStatus == p1.roomMemberStatus;
                }
            })
        }

        @Provides
        @JvmStatic
        @Named(value = IListRoomRecyclerViewAdapter.ROOM_CONTACT)
        fun provideListRoomContactAdapter(appExecutors: AppExecutors): IListRoomRecyclerViewAdapter {
            return ListRoomContactRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<Room>() {
                override fun areItemsTheSame(p0: Room, p1: Room): Boolean {
                    return p0.id == p1.id;
                }

                override fun areContentsTheSame(p0: Room, p1: Room): Boolean {
                    return p0.name == p1.name && p0.updatedDate == p1.updatedDate && p0.avatarUrl == p1.avatarUrl
                            && p0.notifyCount == p1.notifyCount && p0.roomMemberStatus == p1.roomMemberStatus;
                }
            })
        }

        @Provides
        @JvmStatic
        @Named(value = IShowListRoomFragmentFactory.DIRECT_MESSAGE_FRAGMENT_FACTORY)
        fun provideDirectMessageFragmentFactory(): IShowListRoomFragmentFactory {
            return DirectMessageFragmentFactory();
        }

        @Provides
        @JvmStatic
        @Named(value = IShowListRoomFragmentFactory.ROOM_MESSAGE_FRAGMENT_FACTORY)
        fun provideRoomMessageFragmentFactory(): IShowListRoomFragmentFactory {
            return RoomMessageFragmentFactory();
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
        fun provideFavouritesFragmentFactory(): IFragmentFactory {
            return FavouritesFragmentFactory();
        }

        @Provides
        @JvmStatic
        @Named(value = IFragmentFactory.CONTACTS_FRAGMENT)
        fun provideContactsFragmentFactory(): IFragmentFactory {
            return ContactsFragmentFactory();
        }
    }
}