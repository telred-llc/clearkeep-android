package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.SearchActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.SearchActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractSearchActivityModule {
    @ContributesAndroidInjector(modules = [AbstractSearchActivityFragmentsBuilderModule::class, ActivityBindModule::class])
    abstract fun contributeSearchActivity(): SearchActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.SEARCH_ACTIVITY)
        abstract fun bindSearchActivity(activity: SearchActivity): IActivity;

        @Binds
        abstract fun bindSearchActivityViewModelFactory(factory: SearchActivityViewModelFactory): IViewModelFactory<AbstractSearchActivityViewModel>;
    }
}