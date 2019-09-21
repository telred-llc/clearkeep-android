package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.CreateNewCallActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.ICreateNewCallActivity
import vmodev.clearkeep.factories.viewmodels.CreateNewCallActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.ICreateNewCallActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractCreateNewCallActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractCreateNewCallActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeCreateNewCallActivity(): CreateNewCallActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.CREATE_NEW_CALL_ACTIVITY)
        abstract fun bindCreateNewCallActivity(activity: CreateNewCallActivity): IActivity;

        @Binds
        abstract fun bindCreateNewCallActivityViewModelFactory(factory: CreateNewCallActivityViewModelFactory): IViewModelFactory<AbstractCreateNewCallActivityViewModel>;
    }
}