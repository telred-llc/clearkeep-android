package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.ExportKeyActivity
import vmodev.clearkeep.activities.interfaces.IExportKeyActivity
import vmodev.clearkeep.factories.viewmodels.ExportKeyActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IExportKeyActivityViewModeFactory

@Module
@Suppress("unused")
abstract class AbstractExportKeyActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeExportKeyActivity(): ExportKeyActivity;

    @Binds
    abstract fun bindExportKeyActivity(activity: ExportKeyActivity): IExportKeyActivity;

    @Binds
    abstract fun bindExportKeyActivityViewModelFactory(factory: ExportKeyActivityViewModelFactory): IExportKeyActivityViewModeFactory;
}