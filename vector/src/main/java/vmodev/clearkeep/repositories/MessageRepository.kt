package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.databases.AbstractMessageDao
import vmodev.clearkeep.factories.messaghandler.interfaces.IMessageHandlerFactory
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(private val messageDao: AbstractMessageDao, private val matrixMessageHandlerFactory: IMessageHandlerFactory) {
    fun loadListMessageWithRoomId(roomId: String): LiveData<Resource<List<Message>>> {
        return object : AbstractNetworkBoundSourceWithCondition<List<Message>, List<Message>>() {
            override fun saveCallResult(item: List<Message>) {
                messageDao.insertListMessage(item);
            }

            override fun createCall(): LiveData<List<Message>> {
                return LiveDataReactiveStreams.fromPublisher(matrixMessageHandlerFactory.getOrCreateMessageHandler(roomId).getHistoryMessage()
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .toFlowable(BackpressureStrategy.LATEST))
            }

            override fun loadFromDB(): LiveData<List<Message>> {
                return messageDao.getListMessageWithRoomId(roomId);
            }

            override fun checkRemoteSourceWithLocalSource(remoteData: List<Message>, localData: List<Message>): List<Message> {
                if (localData.size == remoteData.size)
                    return ArrayList<Message>();
                else {
                    return remoteData;
                }
            }
        }.asLiveData();
    }
}