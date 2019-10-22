package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.OutgoingCallFragment
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractOutgoingCallActivityFragmentBuilderModule {
    @ContributesAndroidInjector(modules = [AbstractOutgoingFragmentBindModule::class])
    abstract fun contributeOutgoingCallFragment(): OutgoingCallFragment;

    @Module
    abstract class AbstractOutgoingFragmentBindModule {
        @Binds
        @Named(IFragment.OUTGOING_CALL_FRAGMENT)
        abstract fun bindOutgoingCallFragment(fragment : OutgoingCallFragment): IFragment;
    }
}