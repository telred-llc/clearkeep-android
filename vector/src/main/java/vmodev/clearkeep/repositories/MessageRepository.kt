package vmodev.clearkeep.repositories

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.util.Log
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.databases.AbstractMessageDao
import vmodev.clearkeep.factories.messaghandler.interfaces.IMessageHandlerFactory
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser
import vmodev.clearkeep.repositories.wayloads.AbstractLocalLoadSouce
import vmodev.clearkeep.repositories.wayloads.AbstractNetworkBoundSourceWithCondition
import vmodev.clearkeep.repositories.wayloads.AbstractNetworkNonBoundSource
import vmodev.clearkeep.repositories.wayloads.AbstractNetworkNonBoundSourceRx
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(private val messageDao: AbstractMessageDao, private val matrixMessageHandlerFactory: IMessageHandlerFactory, private val matrixService: MatrixService) {
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
        return object : AbstractLocalLoadSouce<List<Message>>() {
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
        return object : AbstractLocalLoadSouce<List<User>>() {
            override fun loadFromDB(): LiveData<List<User>> {
                return messageDao.getUsersInRoom(roomId);
            }
        }.asLiveData();
    }

    fun loadUserByMessageId(messageId: String): LiveData<Resource<User>> {
        return object : AbstractLocalLoadSouce<User>() {
            override fun loadFromDB(): LiveData<User> {
                return messageDao.getUserByMessageId(messageId);
            }
        }.asLiveData();
    }

    fun loadRoomByRoomId(roomId: String): LiveData<Resource<Room>> {
        return object : AbstractLocalLoadSouce<Room>() {
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
        return object : AbstractLocalLoadSouce<List<Message>>() {
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

    fun decryptMessage(messages: List<MessageRoomUser>, type : String): LiveData<Resource<List<MessageRoomUser>>> {
        return object : AbstractNetworkNonBoundSourceRx<List<MessageRoomUser>>() {
            override fun createCall(): Observable<List<MessageRoomUser>> {
                return matrixService.decryptListMessage(messages, type);
            }
        }.asLiveData();
    }
    fun getListMessageRoomUser() : LiveData<Resource<List<MessageRoomUser>>>{
        return object : AbstractLocalLoadSouce<List<MessageRoomUser>>(){
            override fun loadFromDB(): LiveData<List<MessageRoomUser>> {
                return messageDao.getAllMessageWithRoomAndUser();
            }
        }.asLiveData();
    }
}