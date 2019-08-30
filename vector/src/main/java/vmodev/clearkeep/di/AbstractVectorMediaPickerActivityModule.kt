package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import im.vector.activity.VectorMediaPickerActivity
import im.vector.activity.VectorMediaViewerActivity
import vmodev.clearkeep.activities.CallViewActivity
import vmodev.clearkeep.activities.UnifiedSearchActivity

@Module
@Suppress("unused")
abstract class AbstractVectorMediaPickerActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeVectorMediaPickerActivity(): VectorMediaPickerActivity;

    @ContributesAndroidInjector
    abstract fun contributeVectorMediaViewerActivity(): VectorMediaViewerActivity;

    @ContributesAndroidInjector
    abstract fun contributeCallViewActivity(): CallViewActivity;

    @ContributesAndroidInjector
    abstract fun contributeUnifiedSearchActivity(): UnifiedSearchActivity;
}