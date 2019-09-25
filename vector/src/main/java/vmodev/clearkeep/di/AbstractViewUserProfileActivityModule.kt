package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.ViewUserProfileActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IViewUserProfileActivity
import vmodev.clearkeep.factories.viewmodels.ViewUserProfileActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewUserProfileActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractViewUserProfileActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractViewUserProfileActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeViewUserProfileActivity(): ViewUserProfileActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.VIEW_USER_PROFILE_ACTIVITY)
        abstract fun bindViewUserProfileActivity(activity: ViewUserProfileActivity): IActivity;

        @Binds
        abstract fun bindViewUserProfileActivityViewModelFactory(factory: ViewUserProfileActivityViewModelFactory): IViewModelFactory<AbstractViewUserProfileActivityViewModel>;
    }
}