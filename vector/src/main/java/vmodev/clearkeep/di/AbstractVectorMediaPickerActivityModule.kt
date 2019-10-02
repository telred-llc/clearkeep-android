package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import im.vector.activity.*
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

    @ContributesAndroidInjector
    abstract fun contributeSASVerificationActivity() : SASVerificationActivity

    @ContributesAndroidInjector
    abstract fun contributeVectorSharedFilesActivity(): VectorSharedFilesActivity
    @ContributesAndroidInjector
    abstract fun contributeVectorHomeActivity(): VectorHomeActivity
}