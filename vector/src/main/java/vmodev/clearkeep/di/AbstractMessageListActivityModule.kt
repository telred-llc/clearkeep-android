package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.MessageListActivity
import vmodev.clearkeep.activities.interfaces.IMessageListActivity
import vmodev.clearkeep.factories.viewmodels.MessageListActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IMessageListActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractMessageListActivityViewModel

@Module
@Suppress("unused")
abstract class AbstractMessageListActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMessageListActivity(): MessageListActivity;

    @Binds
    abstract fun bindMessageListActivity(activity: MessageListActivity): IMessageListActivity;

    @Binds
    abstract fun bindMessageListActivityViewModelFactory(factory: MessageListActivityViewModelFactory): IMessageListActivityViewModelFactory;
}