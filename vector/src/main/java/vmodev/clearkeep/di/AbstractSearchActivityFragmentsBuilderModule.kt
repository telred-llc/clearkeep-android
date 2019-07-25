package vmodev.clearkeep.di

import android.support.v7.util.DiffUtil
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.fragments.SearchFilesFragment
import vmodev.clearkeep.fragments.SearchMessagesFragment
import vmodev.clearkeep.fragments.SearchPeopleFragment
import vmodev.clearkeep.fragments.SearchRoomsFragment
import vmodev.clearkeep.viewmodelobjects.Room

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

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideListRoomDirectMessageAdapter(appExecutors: AppExecutors): IListRoomRecyclerViewAdapter {
            return ListRoomRecyclerViewAdapter(appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<Room>() {
                override fun areItemsTheSame(p0: Room, p1: Room): Boolean {
                    return p0.id == p1.id;
                }

                override fun areContentsTheSame(p0: Room, p1: Room): Boolean {
                    return p0.name == p1.name && p0.updatedDate == p1.updatedDate && p0.avatarUrl == p1.avatarUrl
                            && p0.notifyCount == p1.notifyCount;
                }
            })
        }
    }
}