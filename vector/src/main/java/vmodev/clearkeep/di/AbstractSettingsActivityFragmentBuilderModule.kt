package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.factories.viewmodels.*
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.*
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.*
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractSettingsActivityFragmentBuilderModule {
    @ContributesAndroidInjector(modules = [FragmentProfileSettingsFragmentBindModule::class])
    abstract fun contributeProfileSettingsFragment(): ProfileSettingsFragment;

    @ContributesAndroidInjector(modules = [FragmentNotificationSettingsBindModule::class])
    abstract fun contributeNotificationSettingsFragment(): NotificationSettingsFragment;

    @ContributesAndroidInjector(modules = [FragmentCallSettingsBindModule::class])
    abstract fun contributeCallSettingsFragment(): CallSettingsFragment;

    @ContributesAndroidInjector(modules = [FragmentReportBindModule::class])
    abstract fun contributeReportFragment(): ReportFragment;

    @ContributesAndroidInjector(modules = [FragmentPrivacyPolicyBindModule::class])
    abstract fun contributePrivacyPolicyFragment(): PrivacyPolicyFragment;

    @ContributesAndroidInjector(modules = [FragmentDeactivateAccountBindModule::class])
    abstract fun contributeDeactivateAccountFragment() : DeactivateAccountFragment;

    @Module
    abstract class FragmentProfileSettingsFragmentBindModule {
        @Binds
        @Named(IFragment.PROFILE_SETTINGS_FRAGMENT)
        abstract fun bindProfileSettingsFragment(fragment: ProfileSettingsFragment): IFragment;

        @Binds
        abstract fun bindProfileSettingsActivityViewModelFactory(factory: ProfileSettingsActivityViewModelFactory): IViewModelFactory<AbstractProfileSettingsActivityViewModel>;
    }

    @Module
    abstract class FragmentNotificationSettingsBindModule {
        @Binds
        @Named(IFragment.NOTIFICATION_SETTINGS_FRAGMENT)
        abstract fun bindNotificationSettingsFragment(fragment: NotificationSettingsFragment): IFragment;

        @Binds
        abstract fun bindNotificationSettingsActivityViewModelFactory(factory: NotificationSettingsViewModelFactory): IViewModelFactory<AbstractNotificationSettingsActivityViewModel>;
    }

    @Module
    abstract class FragmentCallSettingsBindModule {
        @Binds
        @Named(IFragment.CALL_SETTINGS_FRAGMENT)
        abstract fun bindCallSettingsCallFragment(fragment: CallSettingsFragment): IFragment;

        @Binds
        abstract fun bindCallSettingsActivityViewModelFactory(factory: CallSettingsActivityViewModelFactory): IViewModelFactory<AbstractCallSettingActivityViewModel>;
    }

    @Module
    abstract class FragmentReportBindModule {
        @Binds
        @Named(IFragment.REPORT_FRAGMENT)
        abstract fun bindReportFragment(fragment: ReportFragment): IFragment;

        @Binds
        abstract fun bindReportActivityViewModelFactory(factory: ReportActivityViewModelFactory): IViewModelFactory<AbstractReportActivityViewModel>;
    }

    @Module
    abstract class FragmentPrivacyPolicyBindModule {
        @Binds
        @Named(IFragment.PRIVACY_POLICY_FRAGMENT)
        abstract fun bindPrivacyPolicyFragment(fragment: PrivacyPolicyFragment): IFragment;
    }

    @Module
    abstract class FragmentDeactivateAccountBindModule {
        @Binds
        @Named(IFragment.DEACTIVATE_ACCOUNT_FRAGMENT)
        abstract fun bindDeactivateAccountFragment(fragment : DeactivateAccountFragment): IFragment;

        @Binds
        abstract fun bindDeactivateAccountActivityViewModelFactory(factory: DeactivateAccountActivityViewModelFactory): IViewModelFactory<AbstractDeactivateAccountActivityViewModel>;
    }
}