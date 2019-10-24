package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.factories.IncomingCallFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.InProgressCallFragment
import vmodev.clearkeep.fragments.InProgressVoiceCallFragment
import vmodev.clearkeep.fragments.IncomingCallFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractIncomingCallFragmentViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractIncomingCallActivityFragmentBuilderModule {
    @ContributesAndroidInjector(modules = [IncomingCallFragmentBindModule::class])
    abstract fun contributeIncomingCallFragment(): IncomingCallFragment;

    @ContributesAndroidInjector(modules = [InProgressCallFragmentBindModule::class])
    abstract fun contributeInProgressFragment(): InProgressCallFragment;

    @ContributesAndroidInjector(modules = [InProgressVoiceCallFragmentBindModule::class])
    abstract fun contributeInProgressVoiceCCallFragment(): InProgressVoiceCallFragment;

    @Module
    abstract class IncomingCallFragmentBindModule {
        @Binds
        @Named(IFragment.INCOMING_CALL_FRAGMENT)
        abstract fun bindIncomingCallFragment(fragment: IncomingCallFragment): IFragment;

        @Binds
        abstract fun bindIncomingCallFragmentViewModelFactory(factory: IncomingCallFragmentViewModelFactory): IViewModelFactory<AbstractIncomingCallFragmentViewModel>;
    }

    @Module
    abstract class InProgressCallFragmentBindModule {
        @Binds
        @Named(IFragment.IN_PROGRESS_CALL_FRAGMENT)
        abstract fun bindInProgressCallFragment(fragment: InProgressCallFragment): IFragment;
    }

    @Module
    abstract class InProgressVoiceCallFragmentBindModule {
        @Binds
        @Named(IFragment.IN_PROGRESS_VOICE_CALL_FRAGMENT)
        abstract fun bindInProgressVoiceCallFragment(fragment: InProgressVoiceCallFragment): IFragment;
    }
}