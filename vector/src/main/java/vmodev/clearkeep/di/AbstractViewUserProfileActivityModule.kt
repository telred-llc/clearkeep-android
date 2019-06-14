package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.ViewUserProfileActivity
import vmodev.clearkeep.activities.interfaces.IViewUserProfileActivity
import vmodev.clearkeep.factories.viewmodels.ViewUserProfileActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewUserProfileActivityViewModelFactory

@Module
@Suppress("unused")
abstract class AbstractViewUserProfileActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeViewUserProfileActivity(): ViewUserProfileActivity;

    @Binds
    abstract fun bindViewUserProfileActivity(activity: ViewUserProfileActivity): IViewUserProfileActivity;

    @Binds
    abstract fun bindViewUserProfileActivityViewModelFactory(factory: ViewUserProfileActivityViewModelFactory): IViewUserProfileActivityViewModelFactory;
}