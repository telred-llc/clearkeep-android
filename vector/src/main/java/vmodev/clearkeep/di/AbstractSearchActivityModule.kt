package vmodev.clearkeep.di

import android.databinding.DataBindingComponent
import android.support.v4.app.FragmentActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.RoomSettingsActivity
import vmodev.clearkeep.activities.SearchActivity
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.di.scopes.SearchScope
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractSearchActivityModule {
    @ContributesAndroidInjector(modules = [AbstractSearchActivityFragmentsBuilderModule::class])
    abstract fun contributeSearchActivity(): SearchActivity;
//    @Binds
//    @Named("SearchActivity")
//    abstract fun bindSearchActivity(activity: SearchActivity): FragmentActivity;
//
//    @Binds
//    @Named("SearchActivity")
//    abstract fun bindSearchDataBindingComponent(dataBindingCompomemt: ActivityDataBindingComponent): DataBindingComponent;
}