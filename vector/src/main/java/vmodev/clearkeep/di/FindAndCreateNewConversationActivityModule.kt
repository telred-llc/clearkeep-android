package vmodev.clearkeep.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.FindAndCreateNewConversationActivity

@Module
abstract class FindAndCreateNewConversationActivityModule {
    @ContributesAndroidInjector(modules = [])
    abstract fun contributeFindAndCreateNewConversationActivity(): FindAndCreateNewConversationActivity;
}