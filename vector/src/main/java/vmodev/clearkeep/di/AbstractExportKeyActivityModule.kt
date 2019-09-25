package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.ExportKeyActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IExportKeyActivity
import vmodev.clearkeep.factories.viewmodels.ExportKeyActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IExportKeyActivityViewModeFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractExportKeyActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractExportKeyActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeExportKeyActivity(): ExportKeyActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.EXPORT_KEY_ACTIVITY)
        abstract fun bindExportKeyActivity(activity: ExportKeyActivity): IActivity;

        @Binds
        abstract fun bindExportKeyActivityViewModelFactory(factory: ExportKeyActivityViewModelFactory): IViewModelFactory<AbstractExportKeyActivityViewModel>;
    }
}