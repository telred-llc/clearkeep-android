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
    @ContributesAndroidInjector
    abstract fun contributeReportActivity(): ReportActivity;

    @Binds
    abstract fun bindReportActivity(activity: ReportActivity): IReportActivity;

    @Binds
    abstract fun bindReportActivityViewModelFactory(factory: ReportActivityViewModelFactory): IReportActivityViewModelFactory;
}