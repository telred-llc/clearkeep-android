package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import vmodev.clearkeep.factories.messaghandler.MessageHandlerFactory
import vmodev.clearkeep.factories.messaghandler.interfaces.IMessageHandlerFactory
import javax.inject.Singleton

@Module
@Suppress("unused")
abstract class AbstractMatrixSDKModule {
    @Binds
    @Singleton
    abstract fun bindMessageHandlerFactory(factory: MessageHandlerFactory): IMessageHandlerFactory;
}