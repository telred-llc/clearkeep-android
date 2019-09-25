package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.HomeScreenActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.HomeScreenViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractHomeScreenActivityViewModel
import javax.inject.Named

@Suppress("unused")
@Module
abstract class AbstractHomeScreenActivityModule {

    @ContributesAndroidInjector(modules = [HomeScreenActivityFragmentBuilderModule::class, ActivityBindModule::class])
    abstract fun contributeHomeScreenActivityModule(): HomeScreenActivity

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.HOME_SCREEN_ACTIVITY)
        abstract fun bindIHomeScreenActivity(activity: HomeScreenActivity): IActivity;

        @Binds
        abstract fun bindHomeScreenViewModelFactory(factory: HomeScreenViewModelFactory): IViewModelFactory<AbstractHomeScreenActivityViewModel>;
    }
}