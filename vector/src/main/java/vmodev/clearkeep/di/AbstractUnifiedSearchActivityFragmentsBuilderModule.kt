package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.factories.viewmodels.SearchFilesFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.SearchFilesInRoomFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.SearchMessageInRoomFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.SearchFilesFragment
import vmodev.clearkeep.fragments.SearchFilesInRoomFragment
import vmodev.clearkeep.fragments.SearchMessagesInRoomFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchFilesFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchFilesInRoomFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchMessageInroomFragmentViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractUnifiedSearchActivityFragmentsBuilderModule {

    @ContributesAndroidInjector(modules = [FragmentSearchMessageInRoomBindModule::class])
    abstract fun contributeSearchMessageInRoomFragment(): SearchMessagesInRoomFragment;

    @ContributesAndroidInjector(modules = [FragmentSearchFileInRoomBindModule::class])
    abstract fun contributeSearchFilesInRoomFragment(): SearchFilesInRoomFragment;


    @Module
    abstract class FragmentSearchMessageInRoomBindModule {
        @Binds
        @Named(IFragment.SEARCH_MESSAGE_IN_ROOM_FRAGMENT)
        abstract fun bindSearchMessagesInRoomFragment(fragment: SearchMessagesInRoomFragment): IFragment;

        @Binds
        abstract fun bindSearchMessageInRoomFragmentViewModelFactory(factory: SearchMessageInRoomFragmentViewModelFactory): IViewModelFactory<AbstractSearchMessageInroomFragmentViewModel>;
    }

    @Module
    abstract class FragmentSearchFileInRoomBindModule {
        @Binds
        @Named(IFragment.SEARCH_FILES_IN_ROOM_FRAGMENT)
        abstract fun bindSearchFilesInRoomFragment(fragment: SearchFilesInRoomFragment): IFragment;

        @Binds
        abstract fun bindSearchFilesInRoomFragmentViewModelFactory(factory: SearchFilesInRoomFragmentViewModelFactory): IViewModelFactory<AbstractSearchFilesInRoomFragmentViewModel>;
    }
}