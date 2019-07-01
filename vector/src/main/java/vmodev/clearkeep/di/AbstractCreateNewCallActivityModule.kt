package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.CreateNewCallActivity
import vmodev.clearkeep.activities.interfaces.ICreateNewCallActivity
import vmodev.clearkeep.factories.viewmodels.CreateNewCallActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.ICreateNewCallActivityViewModelFactory

@Module
@Suppress("unused")
abstract class AbstractCreateNewCallActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeCreateNewCallActivity(): CreateNewCallActivity;

    @Binds
    abstract fun bindCreateNewCallActivity(activity: CreateNewCallActivity): ICreateNewCallActivity;

    @Binds
    abstract fun bindCreateNewCallActivityViewModelFactory(factory: CreateNewCallActivityViewModelFactory): ICreateNewCallActivityViewModelFactory;
}