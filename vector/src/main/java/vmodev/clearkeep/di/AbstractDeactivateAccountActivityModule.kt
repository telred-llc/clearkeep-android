package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.DeactivateAccountActivity
import vmodev.clearkeep.activities.interfaces.IDeactivateAccountActivity
import vmodev.clearkeep.factories.viewmodels.DeactivateAccountActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IDeactiavateAccountActivityViewModelFactory

@Module
@Suppress("unused")
abstract class AbstractDeactivateAccountActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeDeactivateAccountActivity(): DeactivateAccountActivity;

    @Binds
    abstract fun bindDeactivateAccountActivity(activity: DeactivateAccountActivity): IDeactivateAccountActivity;

    @Binds
    abstract fun bindDeactivateAccountActivityViewModelFactory(factory: DeactivateAccountActivityViewModelFactory): IDeactiavateAccountActivityViewModelFactory;

}