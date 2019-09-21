package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.FindAndCreateNewConversationActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.FindAndCreateNewConversationActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractFindAndCreateNewConversationActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class FindAndCreateNewConversationActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeFindAndCreateNewConversationActivity(): FindAndCreateNewConversationActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.FIND_AND_CREATE_NEW_CONVERSATION_ACTIVITY)
        abstract fun bindFindAndCreateNewConversationActivity(activity: FindAndCreateNewConversationActivity): IActivity;

        @Binds
        abstract fun bindFindAndCreateNewConversationActivityViewModelFactory(factory : FindAndCreateNewConversationActivityViewModelFactory): IViewModelFactory<AbstractFindAndCreateNewConversationActivityViewModel>
    }
}