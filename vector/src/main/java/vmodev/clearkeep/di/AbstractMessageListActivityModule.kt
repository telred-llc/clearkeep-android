package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.MessageListActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IMessageListActivity
import vmodev.clearkeep.factories.viewmodels.MessageListActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IMessageListActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractMessageListActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractMessageListActivityModule {

    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeMessageListActivity(): MessageListActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.MESSAGE_LIST_ACTIVITY)
        abstract fun bindMessageListActivity(activity: MessageListActivity): IActivity;

        @Binds
        abstract fun bindMessageListActivityViewModelFactory(factory: MessageListActivityViewModelFactory): IViewModelFactory<AbstractMessageListActivityViewModel>;
    }
}