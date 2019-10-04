package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.factories.viewmodels.DirectMessageFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.DirectMessageShareFileFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.RoomFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.RoomShareFileFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IDirectMessageFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IRoomFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.DirectMessageFragment
import vmodev.clearkeep.fragments.DirectMessageShareFileFragment
import vmodev.clearkeep.fragments.Interfaces.IDirectMessageFragment
import vmodev.clearkeep.fragments.Interfaces.IDirectMessageShareFileFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.Interfaces.ISearchRoomFragment
import vmodev.clearkeep.fragments.RoomFragment
import vmodev.clearkeep.fragments.RoomShareFileFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractDirectMessageShareFileFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomShareFileFragmentViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractShareFileFragmentModule{
    @ContributesAndroidInjector(modules = [FragmentRoomShareFileBindModule::class])
    abstract fun contributeRoomShareFileFragment(): RoomShareFileFragment;

    @ContributesAndroidInjector(modules = [FragmentDirectShareFileBindModule::class])
    abstract fun contributeDirectMessageShareFileFragment(): DirectMessageShareFileFragment;


    @Module
    abstract class FragmentRoomShareFileBindModule {
        @Binds
        @Named(value = IFragment.ROOM_SHARE_FILE_FRAGMENT)
        abstract fun bindRoomShareFileFragment(fragment: RoomShareFileFragment): IFragment;

        @Binds
        abstract fun bindRoomShareFileFragmentViewModelFactory(factory: RoomShareFileFragmentViewModelFactory): IViewModelFactory<AbstractRoomShareFileFragmentViewModel>;
    }

    @Module
    abstract class FragmentDirectShareFileBindModule {
        @Binds
        @Named(value =IFragment.DIRECT_MESSAGE_SHARE_FILE_FRAGMENT)
        abstract fun bindDirectMessageFragment(fragment: DirectMessageShareFileFragment): IFragment;

        @Binds
        abstract fun bindDirectMessageShareFileFragmentViewModelFactory(factory: DirectMessageShareFileFragmentViewModelFactory): IViewModelFactory<AbstractDirectMessageShareFileFragmentViewModel>;
    }
}