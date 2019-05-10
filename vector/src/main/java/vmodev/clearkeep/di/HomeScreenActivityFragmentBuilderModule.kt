package vmodev.clearkeep.di

import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomRecyclerViewAdapter
import vmodev.clearkeep.binding.FragmentDataBindingComponent
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.fragments.DirectMessageFragment
import vmodev.clearkeep.fragments.FavouritesFragment
import vmodev.clearkeep.fragments.HomeScreenFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.RoomFragment
import vmodev.clearkeep.viewmodelobjects.Room
import javax.inject.Inject
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
                            && p0.notifyCount == p1.notifyCount && p0.roomMemberStatus == p1.roomMemberStatus;
                }
            })
        }
    }
}