package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.HomeScreenActivity

@Suppress("unused")
@Module
abstract class HomeScreenActivityModule {

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeHomeScreenActivityModule() : HomeScreenActivity

}