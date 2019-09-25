package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.PreviewInviteRoomActivity
import vmodev.clearkeep.activities.interfaces.IActivity

@Suppress("unused")
@Module
abstract class PreviewInviteRoomActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModules::class])
    abstract fun contributePreviewInviteRoomActivity(): PreviewInviteRoomActivity;

    @Module
    abstract class ActivityBindModules {
        abstract fun bindPreviewInviteRoomActivity(): IActivity;
    }
}