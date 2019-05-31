package vmodev.clearkeep.factories.messaghandler

import android.app.Application
import vmodev.clearkeep.factories.messaghandler.interfaces.IMessageHandlerFactory
import vmodev.clearkeep.matrixsdk.MatrixMessageHandler
import vmodev.clearkeep.matrixsdk.interfaces.IMatrixMessageHandler
import javax.inject.Inject

class MessageHandlerFactory @Inject constructor(private val context: Application) : IMessageHandlerFactory {

    private val mapMessageHandler = HashMap<String, IMatrixMessageHandler>();

    override fun getOrCreateMessageHandler(roomId: String): IMatrixMessageHandler {
        return if (mapMessageHandler.containsKey(roomId))
            mapMessageHandler[roomId]!!
        else {
            val messageHandler = MatrixMessageHandler(roomId, context);
            mapMessageHandler.put(roomId, messageHandler)
            messageHandler
        }

    }

    override fun unRegisterListener(roomId: String): IMatrixMessageHandler? {
        return mapMessageHandler.remove(roomId);
    }
}