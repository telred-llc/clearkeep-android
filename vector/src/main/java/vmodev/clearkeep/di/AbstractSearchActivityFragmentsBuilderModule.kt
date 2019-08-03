package vmodev.clearkeep.di

import android.support.v7.util.DiffUtil
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.SearchMessageFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.SearchFilesFragment
import vmodev.clearkeep.fragments.SearchMessagesFragment
import vmodev.clearkeep.fragments.SearchPeopleFragment
import vmodev.clearkeep.fragments.SearchRoomsFragment
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchMessageFragmentViewModel
import javax.inject.Named

@Suppress("unused")
@Module
abstract class AbstractSearchActivityFragmentsBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeSearchPeopleFragment(): SearchPeopleFragment;

    @ContributesAndroidInjector
    abstract fun contributeSearchRoomFragment(): SearchRoomsFragment;

    @ContributesAndroidInjector
    abstract fun contributeSearchMessage(): SearchMessagesFragment;

    @ContributesAndroidInjector
    abstract fun contributeSearchFilesFragment(): SearchFilesFragment;

    @Binds
    @Named(IFragment.SEARCH_MESSAGE_FRAGMENT)
    abstract fun bindSearchMessageFragment(fragment: SearchMessagesFragment): IFragment;

    @Binds
    abstract fun bindSearchMessageFragmentViewModelFactory(factory: SearchMessageFragmentViewModelFactory): IViewModelFactory<AbstractSearchMessageFragmentViewModel>;

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideListRoomDirectMessageAdapter(appExecutors: AppExecutors): IListRoomRecyclerViewAdapter {
            return ListRoomRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<RoomListUser>() {
                override fun areItemsTheSame(p0: RoomListUser, p1: RoomListUser): Boolean {
                    return p0.room?.get(0)?.id == p1.room?.get(0)?.id;
                }

                override fun areContentsTheSame(p0: RoomListUser, p1: RoomListUser): Boolean {
                    return p0.room?.get(0)?.name == p1.room?.get(0)?.name && p0.room?.get(0)?.updatedDate == p1.room?.get(0)?.updatedDate && p0.room?.get(0)?.avatarUrl == p1.room?.get(0)?.avatarUrl
                            && p0.room?.get(0)?.notifyCount == p1.room?.get(0)?.notifyCount;
                }
            })
        }
    }
}