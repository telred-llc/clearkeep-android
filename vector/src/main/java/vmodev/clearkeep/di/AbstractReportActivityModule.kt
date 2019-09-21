package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.ReportActivity
import vmodev.clearkeep.activities.interfaces.IReportActivity
import vmodev.clearkeep.factories.viewmodels.ReportActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IReportActivityViewModelFactory

@Module
@Suppress("unused")
abstract class AbstractReportActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeReportActivity(): ReportActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        abstract fun bindReportActivity(activity: ReportActivity): IReportActivity;

        @Binds
        abstract fun bindReportActivityViewModelFactory(factory: ReportActivityViewModelFactory): IReportActivityViewModelFactory;
    }
}