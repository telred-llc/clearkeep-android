package vmodev.clearkeep.factories.messaghandler.interfaces

import vmodev.clearkeep.matrixsdk.interfaces.IMatrixMessageHandler

interface IMessageHandlerFactory {
    fun getOrCreateMessageHandler(roomId: String): IMatrixMessageHandler;
    fun unRegisterListener(roomId: String): IMatrixMessageHandler?;
}