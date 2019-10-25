package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.factories.OutgoingVoiceCallFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.OutgoingVideoCallCallFragment
import vmodev.clearkeep.fragments.OutgoingVoiceCallFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractOutgoingVoiceCallFragmentViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractOutgoingCallActivityFragmentBuilderModule {
    @ContributesAndroidInjector(modules = [AbstractOutgoingVideoCallFragmentBindModule::class])
    abstract fun contributeOutgoingVideoCallFragment(): OutgoingVideoCallCallFragment;

    @ContributesAndroidInjector(modules = [AbstractOutgoingVoiceCallFragmentBindModule::class])
    abstract fun contributeOutgoingVoiceCallFragment(): OutgoingVoiceCallFragment;

    @Module
    abstract class AbstractOutgoingVideoCallFragmentBindModule {
        @Binds
        @Named(IFragment.OUTGOING_CALL_FRAGMENT)
        abstract fun bindOutgoingCallFragment(fragment: OutgoingVideoCallCallFragment): IFragment;
    }

    @Module
    abstract class AbstractOutgoingVoiceCallFragmentBindModule {
        @Binds
        @Named(IFragment.OUTGOING_VOICE_CALL_FRAGMENT)
        abstract fun bindOutgoingVoiceCallFragment(fragment: OutgoingVoiceCallFragment): IFragment;

        @Binds
        abstract fun bindOutgoingVoiceCallFragmentViewModelFactory(factory: OutgoingVoiceCallFragmentViewModelFactory): IViewModelFactory<AbstractOutgoingVoiceCallFragmentViewModel>
    }
}