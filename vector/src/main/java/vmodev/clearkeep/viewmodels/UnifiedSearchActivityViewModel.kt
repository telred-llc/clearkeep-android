 package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.MessageRepository
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchActivityViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractUnifiedSearchActivityViewModel
import javax.inject.Inject

class UnifiedSearchActivityViewModel @Inject constructor(private val messageRepository: MessageRepository) : AbstractUnifiedSearchActivityViewModel() {

    private val _setTimeForRefreshLoadMessage = MutableLiveData<Long>();
    private val _loadMessagesResult = Transformations.switchMap(_setTimeForRefreshLoadMessage) { input ->
        messageRepository.updateListMessage();
        messageRepository.getListMessageInTheRooms()
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
}