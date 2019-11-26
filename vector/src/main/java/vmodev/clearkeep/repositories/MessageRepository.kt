package vmodev.clearkeep.repositories

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.matrix.androidsdk.rest.model.Event
import vmodev.clearkeep.databases.AbstractMessageDao
import vmodev.clearkeep.executors.AppExecutors
import vmodev.clearkeep.factories.messaghandler.interfaces.IMessageHandlerFactory
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.repositories.wayloads.*
import vmodev.clearkeep.rests.models.responses.EditMessageResponse
import vmodev.clearkeep.viewmodelobjects.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(private val messageDao: AbstractMessageDao, private val matrixMessageHandlerFactory: IMessageHandlerFactory, private val matrixService: MatrixService, private val executors: AppExecutors) {
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

    fun loadListMessageFromLocalDBWithRoomId(roomId: String): LiveData<Resource<List<Message>>> {
        return object : AbstractLocalLoadSource<List<Message>>(executors) {
            override fun loadFromDB(): LiveData<List<Message>> {
                return messageDao.getListMessageWithRoomId(roomId);
            }
        }.asLiveData();
    }

    fun registerMatrixMessageHandler(roomId: String): LiveData<Resource<List<Message>>> {
        return object : AbstractNetworkBoundSourceWithCondition<List<Message>, List<Message>>() {
            override fun saveCallResult(item: List<Message>) {
                item.forEach {
                    Log.d("ListInsert", it.roomId + "---" + it.id + "---" + it.userId)
                    messageDao.insert(it);
                }
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
                Log.d("List Size RM", remoteData.size.toString() + "-----" + localData.size.toString());

                if (localData.size == remoteData.size)
                    return ArrayList<Message>();
                else {
                    return remoteData;
                }
            }
        }.asLiveData();
    }

    fun removeMatrixMessageHandler(roomId: String) {
        matrixMessageHandlerFactory.unRegisterListener(roomId);
    }

    fun loadMessageWithId(id: String): LiveData<Resource<Message>> {
        return object : AbstractNetworkBoundSourceWithCondition<Message, Message>() {
            override fun saveCallResult(item: Message) {
                messageDao.insert(item);
            }

            override fun createCall(): LiveData<Message> {
                return messageDao.findById(id);
            }

            override fun loadFromDB(): LiveData<Message> {
                return messageDao.findById(id);
            }

            override fun checkRemoteSourceWithLocalSource(remoteData: Message, localData: Message): Message {
                return localData;
            }
        }.asLiveData();
    }

    fun loadUsersInRoom(roomId: String): LiveData<Resource<List<User>>> {
        return object : AbstractLocalLoadSource<List<User>>(executors) {
            override fun loadFromDB(): LiveData<List<User>> {
                return messageDao.getUsersInRoom(roomId);
            }
        }.asLiveData();
    }

    fun loadUserByMessageId(messageId: String): LiveData<Resource<User>> {
        return object : AbstractLocalLoadSource<User>(executors) {
            override fun loadFromDB(): LiveData<User> {
                return messageDao.getUserByMessageId(messageId);
            }
        }.asLiveData();
    }

    fun loadRoomByRoomId(roomId: String): LiveData<Resource<Room>> {
        return object : AbstractLocalLoadSource<Room>(executors) {
            override fun loadFromDB(): LiveData<Room> {
                return messageDao.getRoomByRoomId(roomId);
            }
        }.asLiveData();
    }

    fun sendTextMessage(roomId: String, content: String): LiveData<Resource<Int>> {
        return object : AbstractNetworkNonBoundSource<Int>() {
            override fun createCall(): LiveData<Int> {
                return LiveDataReactiveStreams.fromPublisher(matrixService.sendTextMessage(roomId, content)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toFlowable(BackpressureStrategy.LATEST))
            }
        }.asLiveData();
    }

    fun getListMessageInTheRooms(): LiveData<Resource<List<Message>>> {
        return object : AbstractLocalLoadSource<List<Message>>(executors) {
            override fun loadFromDB(): LiveData<List<Message>> {
                return messageDao.getAllMessage();
            }
        }.asLiveData();
    }

    @SuppressLint("CheckResult")
    fun updateListMessage() {
        matrixService.getMessagesToSearch().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe {
            Completable.fromAction {
                messageDao.insertListMessage(it);
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
        }
    }

    fun decryptMessage(messages: List<MessageRoomUser>, type: String): LiveData<Resource<List<MessageRoomUser>>> {
        return object : AbstractNetworkNonBoundSourceRx<List<MessageRoomUser>>() {
            override fun createCall(): Observable<List<MessageRoomUser>> {
                return matrixService.decryptListMessage(messages, type);
            }
        }.asLiveData();
    }

    fun getListMessageRoomUser(): LiveData<Resource<List<MessageRoomUser>>> {
        return object : AbstractLocalLoadSource<List<MessageRoomUser>>(executors) {
            override fun loadFromDB(): LiveData<List<MessageRoomUser>> {
                return messageDao.getAllMessageWithRoomAndUser();
            }
        }.asLiveData();
    }

    fun editMessage(event: Event): LiveData<Resource<EditMessageResponse>> {
        return object : AbstractNetworkNonBoundSourceRx<EditMessageResponse>() {
            override fun createCall(): Observable<EditMessageResponse> {
                return matrixService.editMessage(event);
            }
        }.asLiveData();
    }

    fun getLastMessageOfRoom(roomId: String): Observable<Message> {
        return object : AbstractNetworkCallAndSaveToDBReturnRx<Message, Message>() {
            override fun saveCallResult(item: Message) {
                messageDao.insert(item);
            }

            override fun loadFromDb(item: Message): Single<Message> {
                return messageDao.findByIdRx(item.id);
            }

            override fun createCall(): Observable<Message> {
                return matrixService.getLastMessageOfRoom(roomId);
            }
        }.getObject();
    }

    fun editMessageRx(event: Event): Observable<EditMessageResponse> {
        return object : AbstractLoadFromNetworkReturnRx<EditMessageResponse>() {
            override fun createCall(): Observable<EditMessageResponse> {
                return matrixService.editMessage(event);
            }

            override fun saveCallResult(item: EditMessageResponse) {
                //Do something
            }
        }.getObject();
    }

    fun insertMessage(message: Message): Completable {
        return Completable.fromAction { messageDao.insert(message) }
    }

}