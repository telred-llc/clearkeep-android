package vmodev.clearkeep.di

import androidx.recyclerview.widget.DiffUtil
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomContactRecyclerViewAdapter
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.activitiesandfragments.ContactsFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.FavouritesFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.HomeScreenFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.ListRoomFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IFragmentFactory
import vmodev.clearkeep.factories.viewmodels.ContactFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.FavouritesFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.HomeScreenFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.ListRoomFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.*
import vmodev.clearkeep.fragments.ContactsFragment
import vmodev.clearkeep.fragments.FavouritesFragment
import vmodev.clearkeep.fragments.HomeScreenFragment
import vmodev.clearkeep.fragments.Interfaces.*
import vmodev.clearkeep.fragments.ListRoomFragment
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodels.interfaces.AbstractContactFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractFavouritesFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractHomeScreenFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractListRoomFragmentViewModel
import javax.inject.Named

@Suppress("unused")
@Module
abstract class HomeScreenActivityFragmentBuilderModule {
    @ContributesAndroidInjector(modules = [FragmentHomeScreenBindModule::class])
    abstract fun contributeHomeScreenFragment(): HomeScreenFragment;

    @ContributesAndroidInjector(modules = [FragmentFavouritesBindModule::class])
    abstract fun contributeFavouriteFragment(): FavouritesFragment;

    @ContributesAndroidInjector(modules = [FragmentContactsBindModule::class])
    abstract fun contributeContactFragment(): ContactsFragment;

    @ContributesAndroidInjector(modules = [FragmentListRoomBindModule::class])
    abstract fun contributeListRoomFragment(): ListRoomFragment;

    @Module
    abstract class FragmentHomeScreenBindModule {
        @Binds
        @Named(IFragment.HOME_SCREEN_FRAGMENT)
        abstract fun bindHomeScreenFragment(fragment: HomeScreenFragment): IFragment;

        @Binds
        abstract fun bindHomeScreenFragmentViewModelFactory(factory: HomeScreenFragmentViewModelFactory): IViewModelFactory<AbstractHomeScreenFragmentViewModel>;
    }

    @Module
    abstract class FragmentFavouritesBindModule {
        @Binds
        @Named(IFragment.FAVOURITES_FRAGMENT)
        abstract fun bindFavouritesFragment(fragment: FavouritesFragment): IFragment;

        @Binds
        abstract fun bindFavouritesFragmentViewModelFactory(factory: FavouritesFragmentViewModelFactory): IViewModelFactory<AbstractFavouritesFragmentViewModel>;
    }

    @Module
    abstract class FragmentContactsBindModule {
        @Binds
        @Named(IFragment.CONTACTS_FRAGMENT)
        abstract fun bindContactFragment(fragment: ContactsFragment): IFragment;

        @Binds
        abstract fun bindContactFragmentViewModelFactory(factory: ContactFragmentViewModelFactory): IViewModelFactory<AbstractContactFragmentViewModel>;
    }

    @Module
    abstract class FragmentListRoomBindModule {
        @Binds
        @Named(IFragment.LIST_ROOM_FRAGMENT)
        abstract fun bindListRoomFragment(fragment: ListRoomFragment): IFragment;

        @Binds
        abstract fun bindListRoomFragmentViewModelFactory(factory: ListRoomFragmentViewModelFactory): IViewModelFactory<AbstractListRoomFragmentViewModel>;
    }


    @Module
    companion object {
        @Provides
        @JvmStatic
        @Named(value = IListRoomRecyclerViewAdapter.ROOM_CONTACT)
        fun provideListRoomContactAdapter(appExecutors: AppExecutors): IListRoomRecyclerViewAdapter {
            return ListRoomContactRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<RoomListUser>() {
                override fun areItemsTheSame(p0: RoomListUser, p1: RoomListUser): Boolean {
                    return p0.room?.id == p1.room?.id;
                }

                override fun areContentsTheSame(p0: RoomListUser, p1: RoomListUser): Boolean {
                    return p0.room?.name == p1.room?.name && p0.room?.updatedDate == p1.room?.updatedDate && p0.room?.avatarUrl == p1.room?.avatarUrl
                            && p0.room?.notifyCount == p1.room?.notifyCount;
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