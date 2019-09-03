package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import vmodev.clearkeep.factories.messaghandler.MessageHandlerFactory
import vmodev.clearkeep.factories.messaghandler.interfaces.IMessageHandlerFactory
import vmodev.clearkeep.matrixsdk.MatrixEventHandler
import vmodev.clearkeep.matrixsdk.MatrixServiceImplement
import vmodev.clearkeep.matrixsdk.interfaces.IMatrixEventHandler
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import javax.inject.Singleton

@Module
@Suppress("unused")
abstract class AbstractMatrixSDKModule {
    @Binds
    @Singleton
    abstract fun bindMessageHandlerFactory(factory: MessageHandlerFactory): IMessageHandlerFactory;

    @Binds
    @Singleton
    abstract fun bindMatrixEventHandler(matrixEventHandler: MatrixEventHandler): IMatrixEventHandler;

    @Binds
    @Singleton
    abstract fun bindMatrixService(matrixService: MatrixServiceImplement): MatrixService;
}