package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.SearchActivity

@Module
abstract class AbstractSearchActivityModule {
    @ContributesAndroidInjector(modules = [AbstractSearchActivityFragmentsBuilderModule::class])
    abstract fun contributeSearchActivity(): SearchActivity;
}