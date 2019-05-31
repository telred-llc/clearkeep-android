package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.MessageRepository
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractMessageListActivityViewModel
import javax.inject.Inject

class MessageListActivityViewModel @Inject constructor(messageRepository: MessageRepository) : AbstractMessageListActivityViewModel() {
    private val _roomIdForGetListMessage = MutableLiveData<String>();
    private val _getListMessageResult = Transformations.switchMap(_roomIdForGetListMessage) { input -> messageRepository.loadListMessageWithRoomId(input) }

    override fun getListMessageResult(): LiveData<Resource<List<Message>>> {
        return _getListMessageResult;
    }

    override fun setRoomIdForGetListMessage(roomId: String) {
        if (_roomIdForGetListMessage.value != roomId)
            _roomIdForGetListMessage.value = roomId;
    }
}