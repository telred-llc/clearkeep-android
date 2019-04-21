package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.fragments.HomeScreenFragment

@Suppress("unused")
@Module
abstract class HomeScreenActivityFragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeScreenFragment() : HomeScreenFragment;
}