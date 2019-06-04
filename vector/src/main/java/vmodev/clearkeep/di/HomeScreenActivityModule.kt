package vmodev.clearkeep.di

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.HomeScreenActivity
import vmodev.clearkeep.activities.interfaces.IHomeScreenActivity
import vmodev.clearkeep.factories.viewmodels.HomeScreenViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IHomeScreenViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserViewModel

@Suppress("unused")
@Module
abstract class HomeScreenActivityModule {

    @ContributesAndroidInjector(modules = [HomeScreenActivityFragmentBuilderModule::class])
    abstract fun contributeHomeScreenActivityModule(): HomeScreenActivity

    @Binds
    abstract fun bindIHomeScreenActivity(activity: HomeScreenActivity): IHomeScreenActivity;

    @Binds
    abstract fun bindHomeScreenViewModelFactory(factory: HomeScreenViewModelFactory): IHomeScreenViewModelFactory;
}