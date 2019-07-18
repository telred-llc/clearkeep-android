package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.ProfileActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.ProfileActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractProfileActivityViewModel
import javax.inject.Named

@Suppress("unused")
@Module
abstract class AbstractProfileActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeProfileActivity(): ProfileActivity;

    @Binds
    @Named(IActivity.PROFILE_ACTIVITY)
    abstract fun bindProfileActivity(activity: ProfileActivity): IActivity;

    @Binds
    abstract fun bindProfileActivityViewModelFactory(factory: ProfileActivityViewModelFactory): IViewModelFactory<AbstractProfileActivityViewModel>;
}