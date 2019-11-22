package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.PreviewInviteRoomActivity
import vmodev.clearkeep.activities.PreviewJoinActivity
import vmodev.clearkeep.activities.interfaces.IActivity

@Suppress("unused")
@Module
abstract class PreviewJoinActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModules::class])
    abstract fun contributePreviewJoinActivity(): PreviewJoinActivity;

    @Module
    abstract class ActivityBindModules {
        abstract fun bindPreviewJoinActivity(): IActivity;
    }
}