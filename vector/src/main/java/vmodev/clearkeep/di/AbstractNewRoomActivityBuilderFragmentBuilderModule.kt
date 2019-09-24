package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.fragments.InviteUsersToRoomFragment
import vmodev.clearkeep.fragments.CreateNewCallFragment
import vmodev.clearkeep.fragments.CreateNewRoomFragment
import vmodev.clearkeep.fragments.FindAndCreateNewConversationFragment
import vmodev.clearkeep.factories.viewmodels.CreateNewCallActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.CreateNewRoomActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.FindAndCreateNewConversationActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.InviteUsersToRoomViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractCreateNewCallActivityViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractCreateNewRoomActivityViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractFindAndCreateNewConversationActivityViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractInviteUsersToRoomActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractNewRoomActivityBuilderFragmentBuilderModule {
    @ContributesAndroidInjector(modules = [FragmentFindAndCreateNewConversationBindModule::class])
    abstract fun contributeFindAndCreateNewConversationFragment(): FindAndCreateNewConversationFragment;

    @ContributesAndroidInjector(modules = [CreateNewRoomFragmentBindModule::class])
    abstract fun contributeCreateNewRoomFragment(): CreateNewRoomFragment;

    @ContributesAndroidInjector(modules = [CreateNewCallFragmentBindModule::class])
    abstract fun contributeCreateNewCallFragment(): CreateNewCallFragment;

    @ContributesAndroidInjector(modules = [InviteUsersToRoomFragmentBindModule::class])
    abstract fun contributeInviteUsersToRoomFragment(): InviteUsersToRoomFragment;

    @Module
    abstract class FragmentFindAndCreateNewConversationBindModule {
        @Binds
        @Named(IFragment.FIND_AND_CREATE_NEW_CONVERSATION_FRAGMENT)
        abstract fun bindFindAndCreateNewConversationFragment(fragment: FindAndCreateNewConversationFragment): IFragment;

        @Binds
        abstract fun bindFindAndCreateNewConversationActivityViewModelFactory(factory: FindAndCreateNewConversationActivityViewModelFactory): IViewModelFactory<AbstractFindAndCreateNewConversationActivityViewModel>
    }

    @Module
    abstract class CreateNewRoomFragmentBindModule {
        @Binds
        @Named(IFragment.CREATE_NEW_ROOM_FRAGMENT)
        abstract fun bindCreateNewRoomFragment(fragment: CreateNewRoomFragment): IFragment;

        @Binds
        abstract fun bindCreateNewRoomActivityViewModelFactory(factory: CreateNewRoomActivityViewModelFactory): IViewModelFactory<AbstractCreateNewRoomActivityViewModel>;
    }

    @Module
    abstract class CreateNewCallFragmentBindModule {
        @Binds
        @Named(IFragment.CREATE_NEW_CALL_FRAGMENT)
        abstract fun bindCreateNewCallFragment(fragment: CreateNewCallFragment): IFragment;

        @Binds
        abstract fun bindCreateNewCallActivityViewModelFactory(factory: CreateNewCallActivityViewModelFactory): IViewModelFactory<AbstractCreateNewCallActivityViewModel>;
    }

    @Module
    abstract class InviteUsersToRoomFragmentBindModule {
        @Binds
        @Named(IFragment.INVITE_USERS_TO_ROOM_FRAGMENT)
        abstract fun bindInviteUsersToRoomFragment(fragment: InviteUsersToRoomFragment): IFragment;

        @Binds
        abstract fun bindInviteUsersToRoomActivityViewModelFactory(factory: InviteUsersToRoomViewModelFactory): IViewModelFactory<AbstractInviteUsersToRoomActivityViewModel>;
    }
}