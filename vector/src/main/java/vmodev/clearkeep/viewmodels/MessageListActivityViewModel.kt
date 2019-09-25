package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.MessageRepository
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractMessageListActivityViewModel
import javax.inject.Inject

class MessageListActivityViewModel @Inject constructor(private val messageRepository: MessageRepository) : AbstractMessageListActivityViewModel() {
    private val _roomIdForGetListMessage = MutableLiveData<String>();
    private val _messageId = MutableLiveData<String>();
    private val _getListMessageResult = Transformations.switchMap(_roomIdForGetListMessage) { input -> messageRepository.loadListMessageFromLocalDBWithRoomId(input) }
    private val _getMessage = Transformations.switchMap(_roomIdForGetListMessage) { input -> messageRepository.loadMessageWithId(input) }
    private val _getRoomResult = Transformations.switchMap(_roomIdForGetListMessage) { input -> messageRepository.loadRoomByRoomId(input) }
    private val _registerMatrixMessageHandlerResult = Transformations.switchMap(_roomIdForGetListMessage) { input -> messageRepository.registerMatrixMessageHandler(input) }
    private val _getUsersByRoomId = Transformations.switchMap(_roomIdForGetListMessage) { input -> messageRepository.loadUsersInRoom(input) }
    private val _setSendMessage = MutableLiveData<SendMessageObject>();
    private val _getSendMessageResult = Transformations.switchMap(_setSendMessage) { input -> messageRepository.sendTextMessage(input.roomId, input.content) }

    override fun getListMessageResult(): LiveData<Resource<List<Message>>> {
        return _getListMessageResult;
    }

    override fun setRoomIdForGetListMessage(roomId: String) {
        _roomIdForGetListMessage.value = roomId;
    }

    override fun setMessageId(id: String) {
        _messageId.value = id;
    }

    override fun getMessageResult(): LiveData<Resource<Message>> {
        return _getMessage;
    }

    override fun getRoomResult(): LiveData<Resource<Room>> {
        return _getRoomResult;
    }

    override fun removeMatrixMessageHandler(roomId: String) {
        messageRepository.removeMatrixMessageHandler(roomId);
    }

    override fun registerMatrixMessageHandlerResult(): LiveData<Resource<List<Message>>> {
        return _registerMatrixMessageHandlerResult;
    }

    override fun getUsersByRoomIdResult(): LiveData<Resource<List<User>>> {
        return _getUsersByRoomId;
    }

    override fun setSendMessage(roomId: String, content: String) {
        _setSendMessage.value = SendMessageObject(roomId, content);
    }

    override fun getSendMessageResult(): LiveData<Resource<Int>> {
        return _getSendMessageResult;
    }
}