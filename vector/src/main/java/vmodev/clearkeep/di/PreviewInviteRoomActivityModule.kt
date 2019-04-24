package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.PreviewInviteRoomActivity

@Suppress("unused")
@Module
abstract class PreviewInviteRoomActivityModule {
    @ContributesAndroidInjector(modules = [])
    abstract fun contributePreviewInviteRoomActivity(): PreviewInviteRoomActivity;
}