package vmodev.clearkeep.di

import android.support.v7.util.DiffUtil
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.UserInformationActivity
import vmodev.clearkeep.activities.interfaces.IUserInformationActivity
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.adapters.ListRoomRecyclerViewAdapter
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.viewmodels.UserInformationActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IUserInformationActivityViewModelFactory
import vmodev.clearkeep.viewmodelobjects.Room
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractUserInformationActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeUserInformationActivity(): UserInformationActivity;

    @Binds
    abstract fun bindUserInformationActivity(activity: UserInformationActivity): IUserInformationActivity;

    @Binds
    abstract fun bindUSerInformationActivityViewModelFactory(factory: UserInformationActivityViewModelFactory): IUserInformationActivityViewModelFactory;

//    @Module
//    companion object {
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
//    }
}