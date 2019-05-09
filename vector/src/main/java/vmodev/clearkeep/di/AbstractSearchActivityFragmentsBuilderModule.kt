package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.fragments.SearchFilesFragment
import vmodev.clearkeep.fragments.SearchMessagesFragment
import vmodev.clearkeep.fragments.SearchPeopleFragment
import vmodev.clearkeep.fragments.SearchRoomsFragment

@Suppress("unused")
@Module
abstract class AbstractSearchActivityFragmentsBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeSearchPeopleFragment(): SearchPeopleFragment;

    @ContributesAndroidInjector
    abstract fun contributeSearchRoomFragment(): SearchRoomsFragment;

    @ContributesAndroidInjector
    abstract fun contributeSearchMessage() : SearchMessagesFragment;
    @ContributesAndroidInjector
    abstract fun contributeSearchFilesFragment() : SearchFilesFragment;
}