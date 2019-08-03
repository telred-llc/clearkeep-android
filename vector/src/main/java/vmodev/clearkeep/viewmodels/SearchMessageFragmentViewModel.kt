package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser
import vmodev.clearkeep.repositories.MessageRepository
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchMessageFragmentViewModel
import javax.inject.Inject

class SearchMessageFragmentViewModel @Inject constructor(private val messageRepository: MessageRepository) : AbstractSearchMessageFragmentViewModel() {
    private val _setTimeForRefreshLoadMessage = MutableLiveData<Long>();
    private val _loadMessagesResult = Transformations.switchMap(_setTimeForRefreshLoadMessage) { input ->
        messageRepository.updateListMessage();
        messageRepository.getListMessageInTheRooms()
    }
    private val _loadMessagesRoomUserResult = Transformations.switchMap(_setTimeForRefreshLoadMessage) { input ->
        messageRepository.updateListMessage();
        messageRepository.getListMessageRoomUser()
    }

    override fun setTimeForRefreshLoadMessage(time: Long) {
        if (_setTimeForRefreshLoadMessage.value != time)
            _setTimeForRefreshLoadMessage.value = time;
    }

    override fun getLoadMessagesResult(): LiveData<Resource<List<Message>>> {
        return _loadMessagesResult;
    }

    override fun decryptListMessage(messages: List<MessageRoomUser>): LiveData<Resource<List<MessageRoomUser>>> {
        return messageRepository.decryptMessage(messages);
    }

    override fun getListMessageRoomUser(): LiveData<Resource<List<MessageRoomUser>>> {
        return _loadMessagesRoomUserResult;
    }
}