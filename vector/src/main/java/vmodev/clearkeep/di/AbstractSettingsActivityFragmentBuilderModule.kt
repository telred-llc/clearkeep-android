package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.*
import vmodev.clearkeep.factories.viewmodels.*
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.*
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractSettingsActivityFragmentBuilderModule {
    @ContributesAndroidInjector(modules = [FragmentProfileSettingsFragmentBindModule::class])
    abstract fun contributeProfileSettingsFragment(): ProfileSettingsActivity;

    @ContributesAndroidInjector(modules = [FragmentNotificationSettingsBindModule::class])
    abstract fun contributeNotificationSettingsFragment(): NotificationSettingsActivity;

    @ContributesAndroidInjector(modules = [FragmentCallSettingsBindModule::class])
    abstract fun contributeCallSettingsFragment(): CallSettingsActivity;

    @ContributesAndroidInjector(modules = [FragmentReportBindModule::class])
    abstract fun contributeReportFragment(): ReportActivity;

    @ContributesAndroidInjector(modules = [FragmentPrivacyPolicyBindModule::class])
    abstract fun contributePrivacyPolicyFragment(): PrivacyPolicyActivity;

    @ContributesAndroidInjector(modules = [FragmentDeactivateAccountBindModule::class])
    abstract fun contributeDeactivateAccountFragment() : DeactivateAccountActivity;

    @Module
    abstract class FragmentProfileSettingsFragmentBindModule {
        @Binds
        @Named(IFragment.PROFILE_SETTINGS_FRAGMENT)
        abstract fun bindProfileSettingsFragment(fragment: ProfileSettingsActivity): IFragment;

        @Binds
        abstract fun bindProfileSettingsActivityViewModelFactory(factory: ProfileSettingsActivityViewModelFactory): IViewModelFactory<AbstractProfileSettingsActivityViewModel>;
    }

    @Module
    abstract class FragmentNotificationSettingsBindModule {
        @Binds
        @Named(IFragment.NOTIFICATION_SETTINGS_FRAGMENT)
        abstract fun bindNotificationSettingsFragment(fragment: NotificationSettingsActivity): IFragment;

        @Binds
        abstract fun bindNotificationSettingsActivityViewModelFactory(factory: NotificationSettingsViewModelFactory): IViewModelFactory<AbstractNotificationSettingsActivityViewModel>;
    }

    @Module
    abstract class FragmentCallSettingsBindModule {
        @Binds
        @Named(IFragment.CALL_SETTINGS_FRAGMENT)
        abstract fun bindCallSettingsCallFragment(fragment: CallSettingsActivity): IFragment;

        @Binds
        abstract fun bindCallSettingsActivityViewModelFactory(factory: CallSettingsActivityViewModelFactory): IViewModelFactory<AbstractCallSettingActivityViewModel>;
    }

    @Module
    abstract class FragmentReportBindModule {
        @Binds
        @Named(IFragment.REPORT_FRAGMENT)
        abstract fun bindReportFragment(fragment: ReportActivity): IFragment;

        @Binds
        abstract fun bindReportActivityViewModelFactory(factory: ReportActivityViewModelFactory): IViewModelFactory<AbstractReportActivityViewModel>;
    }

    @Module
    abstract class FragmentPrivacyPolicyBindModule {
        @Binds
        @Named(IFragment.PRIVACY_POLICY_FRAGMENT)
        abstract fun bindPrivacyPolicyFragment(fragment: PrivacyPolicyActivity): IFragment;
    }

    @Module
    abstract class FragmentDeactivateAccountBindModule {
        @Binds
        @Named(IFragment.DEACTIVATE_ACCOUNT_FRAGMENT)
        abstract fun bindDeactivateAccountFragment(fragment : DeactivateAccountActivity): IFragment;

        @Binds
        abstract fun bindDeactivateAccountActivityViewModelFactory(factory: DeactivateAccountActivityViewModelFactory): IViewModelFactory<AbstractDeactivateAccountActivityViewModel>;
    }
}