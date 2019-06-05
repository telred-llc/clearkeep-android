package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.MessageRepository
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodels.interfaces.AbstractMessageListActivityViewModel
import javax.inject.Inject

class MessageListActivityViewModel @Inject constructor(messageRepository: MessageRepository, roomRepository: RoomRepository) : AbstractMessageListActivityViewModel() {
    private val _roomIdForGetListMessage = MutableLiveData<String>();
    private val _messageId = MutableLiveData<String>();
    private val _getListMessageResult = Transformations.switchMap(_roomIdForGetListMessage) { input -> messageRepository.loadListMessageWithRoomId(input) }
    private val _getMessage = Transformations.switchMap(_roomIdForGetListMessage) { input -> messageRepository.loadMessageWithId(input) }
    private val _getRoomResult = Transformations.switchMap(_roomIdForGetListMessage) { input -> roomRepository.loadRoom(input) }

    override fun getListMessageResult(): LiveData<Resource<List<Message>>> {
        return _getListMessageResult;
    }

    override fun setRoomIdForGetListMessage(roomId: String) {
//        if (_roomIdForGetListMessage.value != roomId)
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

    override fun setRoomId(roomId: String) {

    }
}