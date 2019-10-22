package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.fragments.InProgressCallFragment
import vmodev.clearkeep.fragments.IncomingCallFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractIncomingCallActivityFragmentBuilderModule {
    @ContributesAndroidInjector(modules = [IncomingCallFragmentBindModule::class])
    abstract fun contributeIncomingCallFragment(): IncomingCallFragment;

    @ContributesAndroidInjector(modules = [InProgressCallFragmentBindModule::class])
    abstract fun contributeInProgressFragment(): InProgressCallFragment;

    @Module
    abstract class IncomingCallFragmentBindModule {
        @Binds
        @Named(IFragment.INCOMING_CALL_FRAGMENT)
        abstract fun bindIncomingCallFragment(fragment: IncomingCallFragment): IFragment;
    }

    @Module
    abstract class InProgressCallFragmentBindModule {
        @Binds
        @Named(IFragment.IN_PROGRESS_CALL_FRAGMENT)
        abstract fun bindInProgressCallFragment(fragment: InProgressCallFragment): IFragment;
    }
}