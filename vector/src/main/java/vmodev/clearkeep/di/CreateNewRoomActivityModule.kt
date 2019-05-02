package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.CreateNewRoomActivity

@Module
abstract class CreateNewRoomActivityModule {
    @ContributesAndroidInjector(modules = [])
    abstract fun contributeCreateNewRoomActivity(): CreateNewRoomActivity;
}