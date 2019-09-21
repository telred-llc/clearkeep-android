package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.DeactivateAccountActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.DeactivateAccountActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractDeactivateAccountActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractDeactivateAccountActivityModule {

    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeDeactivateAccountActivity(): DeactivateAccountActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.DEACTIVATE_ACCOUNT_ACTIVITY)
        abstract fun bindDeactivateAccountActivity(activity: DeactivateAccountActivity): IActivity;

        @Binds
        abstract fun bindDeactivateAccountActivityViewModelFactory(factory: DeactivateAccountActivityViewModelFactory): IViewModelFactory<AbstractDeactivateAccountActivityViewModel>;
    }
}