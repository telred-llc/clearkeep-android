package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.ReportActivity
import vmodev.clearkeep.activities.interfaces.IReportActivity

@Module
@Suppress("unused")
abstract class AbstractReportActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeReportActivity(): ReportActivity;

    @Binds
    abstract fun bindReportActivity(activity: ReportActivity): IReportActivity;
}