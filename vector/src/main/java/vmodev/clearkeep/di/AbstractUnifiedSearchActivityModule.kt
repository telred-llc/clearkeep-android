package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.UnifiedSearchActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.UnifiedSearchActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractUnifiedSearchActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractUnifiedSearchActivityModule {
    @ContributesAndroidInjector(modules = [ ActivitySearchRoomBindModule::class, AbstractUnifiedSearchActivityFragmentsBuilderModule::class])
    abstract fun unifiedSearchInRoomActivity(): UnifiedSearchActivity

    @Module
    abstract class ActivitySearchRoomBindModule {
        @Binds
        @Named(IActivity.UNIFIED_SEARCH_ACTIVITY)
        abstract fun bindUnifiedSearchActivity(activity: UnifiedSearchActivity): IActivity

        @Binds
        abstract fun bindUnifiedSearchActivityViewModelFactory(factory: UnifiedSearchActivityViewModelFactory): IViewModelFactory<AbstractUnifiedSearchActivityViewModel>
    }
}