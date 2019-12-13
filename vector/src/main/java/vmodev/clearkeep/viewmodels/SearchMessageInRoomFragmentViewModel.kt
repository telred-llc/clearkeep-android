package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser
import vmodev.clearkeep.repositories.MessageRepository
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchMessageFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchMessageInroomFragmentViewModel
import javax.inject.Inject

class SearchMessageInRoomFragmentViewModel @Inject constructor(private val messageRepository: MessageRepository) : AbstractSearchMessageInroomFragmentViewModel() {


    private var _roomId : String = ""
    private val _setTimeForRefreshLoadMessage = MutableLiveData<Long>();
    private val _loadMessagesResult = Transformations.switchMap(_setTimeForRefreshLoadMessage) { input ->
        messageRepository.updateListMessage();
        messageRepository.getListMessageInTheRoom(_roomId)
    }
    private val _loadMessagesRoomUserResult = Transformations.switchMap(_setTimeForRefreshLoadMessage) { input ->
        messageRepository.updateListMessage();
        messageRepository.getListMessageRoomUserInRoom(_roomId)
    }

    override fun setTimeForRefreshLoadMessage(time: Long) {
        if (_setTimeForRefreshLoadMessage.value != time)
            _setTimeForRefreshLoadMessage.value = time;
    }

    override fun getLoadMessagesResult(): LiveData<Resource<List<Message>>> {
        return _loadMessagesResult;
    }

    override fun decryptListMessage(messages: List<MessageRoomUser>): LiveData<Resource<List<MessageRoomUser>>> {
        return messageRepository.decryptMessage(messages, "m.text");
    }


    override fun getRoomId(roomId: String) {
        _roomId =roomId
    }
    override fun getListMessageRoomUserInRoom(): LiveData<Resource<List<MessageRoomUser>>> {
        return _loadMessagesRoomUserResult;
    }

}