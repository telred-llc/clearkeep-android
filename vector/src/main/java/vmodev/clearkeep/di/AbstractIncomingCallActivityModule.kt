package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.IncomingCallActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractIncomingCallActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class, AbstractIncomingCallActivityFragmentBuilderModule::class])
    abstract fun contributeIncomingCallActivity(): IncomingCallActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.INCOMING_CALL_ACTIVITY)
        abstract fun bindIncomingCallActivity(activity: IncomingCallActivity): IActivity;
    }
}