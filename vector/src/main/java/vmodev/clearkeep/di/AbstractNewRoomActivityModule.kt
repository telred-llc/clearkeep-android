package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.NewRoomActivity

@Module
@Suppress("unused")
abstract class AbstractNewRoomActivityModule {
    @ContributesAndroidInjector(modules = [HomeScreenActivityFragmentBuilderModule::class])
    abstract fun contributeNewRoomModule() : NewRoomActivity;
}