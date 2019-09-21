package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.PrivacyPolicyActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IPrivacyPolicyActivity
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractPrivacyPolicyActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributePrivacyPolicyActivity(): PrivacyPolicyActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.PRIVACY_POLICY_ACTIVITY)
        abstract fun bindPrivacyPolicyActivity(activity: PrivacyPolicyActivity): IActivity;
    }
}