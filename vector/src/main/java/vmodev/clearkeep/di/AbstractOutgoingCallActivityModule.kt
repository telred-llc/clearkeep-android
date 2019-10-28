package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.OutgoingCallActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractOutgoingCallActivityModule {

    @ContributesAndroidInjector(modules = [ActivityBindModule::class, AbstractOutgoingCallActivityFragmentBuilderModule::class])
    abstract fun contributeOutgoingCallActivity(): OutgoingCallActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.OUTGOING_CALL_ACTIVITY)
        abstract fun bindOutgoingCallActivity(activity : OutgoingCallActivity): IActivity;
    }

}