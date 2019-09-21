package vmodev.clearkeep.di

import androidx.recyclerview.widget.DiffUtil
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomRecyclerViewAdapter
import vmodev.clearkeep.bindingadapters.interfaces.IDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.SearchFilesFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.SearchMessageFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.SearchPeopleFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.SearchRoomsFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.SearchFilesFragment
import vmodev.clearkeep.fragments.SearchMessagesFragment
import vmodev.clearkeep.fragments.SearchPeopleFragment
import vmodev.clearkeep.fragments.SearchRoomsFragment
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchFilesFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchMessageFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchPeopleFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchRoomsFragmentViewModel
import javax.inject.Named

@Suppress("unused")
@Module
abstract class AbstractSearchActivityFragmentsBuilderModule {
    @ContributesAndroidInjector(modules = [FragmentSearchPeopleBindModule::class])
    abstract fun contributeSearchPeopleFragment(): SearchPeopleFragment;

    @ContributesAndroidInjector(modules = [FragmentSearchRoomBindModule::class])
    abstract fun contributeSearchRoomFragment(): SearchRoomsFragment;

    @ContributesAndroidInjector(modules = [FragmentSearchMessageBindModule::class])
    abstract fun contributeSearchMessage(): SearchMessagesFragment;

    @ContributesAndroidInjector(modules = [FragmentSearchFileBindModule::class])
    abstract fun contributeSearchFilesFragment(): SearchFilesFragment;

    @Module
    abstract class FragmentSearchMessageBindModule {
        @Binds
        @Named(IFragment.SEARCH_MESSAGE_FRAGMENT)
        abstract fun bindSearchMessageFragment(fragment: SearchMessagesFragment): IFragment;

        @Binds
        abstract fun bindSearchMessageFragmentViewModelFactory(factory: SearchMessageFragmentViewModelFactory): IViewModelFactory<AbstractSearchMessageFragmentViewModel>;

    }

    @Module
    abstract class FragmentSearchRoomBindModule {
        @Binds
        @Named(IFragment.SEARCH_ROOM_FRAGMENT)
        abstract fun bindSearchRoomFragment(fragment: SearchRoomsFragment): IFragment;

        @Binds
        abstract fun bindSearchRoomFragmentViewModelFactory(factory: SearchRoomsFragmentViewModelFactory): IViewModelFactory<AbstractSearchRoomsFragmentViewModel>;
    }

    @Module
    abstract class FragmentSearchPeopleBindModule {
        @Binds
        @Named(IFragment.SEARCH_PEOPLE_FRAGMENT)
        abstract fun bindSearchPeopleFragment(fragment: SearchPeopleFragment): IFragment;

        @Binds
        abstract fun bindSearchPeopleFragmentViewModelFactory(factory: SearchPeopleFragmentViewModelFactory): IViewModelFactory<AbstractSearchPeopleFragmentViewModel>;
    }

    @Module
    abstract class FragmentSearchFileBindModule {
        @Binds
        @Named(IFragment.SEARCH_FILES_FRAGMENT)
        abstract fun bindSearchFilesFragment(fragment: SearchFilesFragment): IFragment;

        @Binds
        abstract fun bindSearchFilesFragmentViewModelFactory(factory: SearchFilesFragmentViewModelFactory): IViewModelFactory<AbstractSearchFilesFragmentViewModel>;
    }


    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideListRoomDirectMessageAdapter(appExecutors: AppExecutors, dataBindingComponent : IDataBindingComponent): IListRoomRecyclerViewAdapter {
            return ListRoomRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<RoomListUser>() {
                override fun areItemsTheSame(p0: RoomListUser, p1: RoomListUser): Boolean {
                    return p0.room?.id == p1.room?.id;
                }

                override fun areContentsTheSame(p0: RoomListUser, p1: RoomListUser): Boolean {
                    return p0.room?.name == p1.room?.name && p0.room?.updatedDate == p1.room?.updatedDate && p0.room?.avatarUrl == p1.room?.avatarUrl
                            && p0.room?.notifyCount == p1.room?.notifyCount;
                }
            }, dataBindingComponent = dataBindingComponent)
        }
    }
}