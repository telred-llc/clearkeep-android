package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.PrivacyPolicyActivity
import vmodev.clearkeep.activities.interfaces.IPrivacyPolicyActivity

@Module
@Suppress("unused")
abstract class AbstractPrivacyPolicyActivityModule {
    @ContributesAndroidInjector
    abstract fun contributePrivacyPolicyActivity(): PrivacyPolicyActivity;

    @Binds
    abstract fun bindPrivacyPolicyActivity(activity: PrivacyPolicyActivity): IPrivacyPolicyActivity;
}