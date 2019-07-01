package vmodev.clearkeep.factories.messaghandler

import android.app.Application
import vmodev.clearkeep.applications.ClearKeepApplication
import vmodev.clearkeep.databases.AbstractMessageDao
import vmodev.clearkeep.factories.messaghandler.interfaces.IMessageHandlerFactory
import vmodev.clearkeep.matrixsdk.MatrixMessageHandler
import vmodev.clearkeep.matrixsdk.interfaces.IMatrixMessageHandler
import vmodev.clearkeep.repositories.MessageRepository
import javax.inject.Inject

class MessageHandlerFactory @Inject constructor(private val context: ClearKeepApplication, private val messageDao: AbstractMessageDao) : IMessageHandlerFactory {

    private val mapMessageHandler = HashMap<String, IMatrixMessageHandler>();

    override fun getOrCreateMessageHandler(roomId: String): IMatrixMessageHandler {
        return if (mapMessageHandler.containsKey(roomId))
            mapMessageHandler[roomId]!!
        else {
            val messageHandler = MatrixMessageHandler(roomId, context, messageDao);
            mapMessageHandler.put(roomId, messageHandler)
            messageHandler
        }

    }

    override fun unRegisterListener(roomId: String): IMatrixMessageHandler? {
        return mapMessageHandler.remove(roomId);
    }
}