package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.fragments.DirectMessageFragment
import vmodev.clearkeep.fragments.FavouritesFragment
import vmodev.clearkeep.fragments.HomeScreenFragment
import vmodev.clearkeep.fragments.RoomFragment

@Suppress("unused")
@Module
abstract class HomeScreenActivityFragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeScreenFragment() : HomeScreenFragment;
    @ContributesAndroidInjector
    abstract fun contributeRoomFragment() : RoomFragment;
    @ContributesAndroidInjector
    abstract fun contributeDirectMessageFragment() : DirectMessageFragment;
    @ContributesAndroidInjector
    abstract fun contributeFavouriteFragment() : FavouritesFragment;
}