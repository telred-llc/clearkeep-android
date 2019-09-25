package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.dialogfragments.ReceivedShareFileDialogFragment
import vmodev.clearkeep.factories.viewmodels.DirectMessageFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.RoomFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IDirectMessageFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IRoomFragmentViewModelFactory
import vmodev.clearkeep.fragments.DirectMessageFragment
import vmodev.clearkeep.fragments.Interfaces.IDirectMessageFragment
import vmodev.clearkeep.fragments.Interfaces.ISearchRoomFragment
import vmodev.clearkeep.fragments.RoomFragment
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractDialogFragmentModules {
    @ContributesAndroidInjector
    abstract fun contributeRoomFragment(): RoomFragment;

    @ContributesAndroidInjector
    abstract fun contributeDirectMessageFragment(): DirectMessageFragment;

    @ContributesAndroidInjector
    abstract fun contributeReceivedShareFileDialogFragment(): ReceivedShareFileDialogFragment;

    @Binds
    @Named(ISearchRoomFragment.SEARCH_ROOM_FRAGMENT)
    abstract fun bindRoomFragment(fragment: RoomFragment): ISearchRoomFragment;

    @Binds
    abstract fun bindDirectMessageFragment(fragment: DirectMessageFragment): IDirectMessageFragment;

    @Binds
    abstract fun bindDirectMessageFragmentViewModelFactory(factory: DirectMessageFragmentViewModelFactory): IDirectMessageFragmentViewModelFactory;

    @Binds
    abstract fun bindRoomFragmentViewModelFactory(factory: RoomFragmentViewModelFactory): IRoomFragmentViewModelFactory;
}