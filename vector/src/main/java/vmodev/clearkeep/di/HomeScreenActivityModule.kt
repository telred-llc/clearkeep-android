package vmodev.clearkeep.di

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.HomeScreenActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IHomeScreenActivity
import vmodev.clearkeep.factories.viewmodels.HomeScreenViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IHomeScreenViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractHomeScreenActivityViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserViewModel
import javax.inject.Named

@Suppress("unused")
@Module
abstract class HomeScreenActivityModule {

    @ContributesAndroidInjector(modules = [HomeScreenActivityFragmentBuilderModule::class])
    abstract fun contributeHomeScreenActivityModule(): HomeScreenActivity

    @Binds
    @Named(IActivity.HOME_SCREEN_ACTIVITY)
    abstract fun bindIHomeScreenActivity(activity: HomeScreenActivity): IActivity;

    @Binds
    abstract fun bindHomeScreenViewModelFactory(factory: HomeScreenViewModelFactory): IViewModelFactory<AbstractHomeScreenActivityViewModel>;
}