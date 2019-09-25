package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.UserInformationActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IUserInformationActivity
import vmodev.clearkeep.factories.viewmodels.UserInformationActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IUserInformationActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserInformationActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractUserInformationActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeUserInformationActivity(): UserInformationActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.USER_INFORMATION_ACTIVITY)
        abstract fun bindUserInformationActivity(activity: UserInformationActivity): IActivity;

        @Binds
        abstract fun bindUSerInformationActivityViewModelFactory(factory: UserInformationActivityViewModelFactory): IViewModelFactory<AbstractUserInformationActivityViewModel>;
    }
}