package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import org.matrix.androidsdk.rest.model.Event
import vmodev.clearkeep.repositories.MessageRepository
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractCallHistoryViewModel
import javax.inject.Inject

class CallHistoryFragmentViewModel @Inject constructor(private val messageRepository: MessageRepository) : AbstractCallHistoryViewModel() {

    private val _setTimeForRefreshLoadMessage = MutableLiveData<Long>()
    private val _loadMessagesResult = Transformations.switchMap(_setTimeForRefreshLoadMessage) { input ->
        messageRepository.updateListMessage()
        messageRepository.getListMessageInTheRooms()
    }


    private val _loadMessagesRoomUserResult = Transformations.switchMap(_setTimeForRefreshLoadMessage) { input ->
        messageRepository.updateListMessage()
        messageRepository.getListMessageRoomUser()
    }

    override fun getListMessageRoomUser(): LiveData<Resource<List<MessageRoomUser>>> {
        return _loadMessagesRoomUserResult
    }

    override fun getListCallHistory(decryptListMessage: List<MessageRoomUser>): LiveData<Resource<List<MessageRoomUser>>> {
        return messageRepository.decryptMessage(decryptListMessage, Event.EVENT_TYPE_CALL_INVITE)
    }
    override fun setTimeForRefreshLoadMessage(time: Long) {
        if (_setTimeForRefreshLoadMessage.value != time)
            _setTimeForRefreshLoadMessage.value = time
    }
}