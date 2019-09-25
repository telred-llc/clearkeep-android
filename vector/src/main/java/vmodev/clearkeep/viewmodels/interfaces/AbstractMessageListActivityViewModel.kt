package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractMessageListActivityViewModel : ViewModel() {
    abstract fun getListMessageResult(): LiveData<Resource<List<Message>>>;
    abstract fun setRoomIdForGetListMessage(roomId: String);
    abstract fun setMessageId(id: String);
    abstract fun getMessageResult(): LiveData<Resource<Message>>;
    abstract fun getRoomResult(): LiveData<Resource<Room>>;
    abstract fun removeMatrixMessageHandler(roomId: String);
    abstract fun registerMatrixMessageHandlerResult(): LiveData<Resource<List<Message>>>;
    abstract fun getUsersByRoomIdResult(): LiveData<Resource<List<User>>>;
    abstract fun setSendMessage(roomId: String, content: String);
    abstract fun getSendMessageResult(): LiveData<Resource<Int>>;

    data class SendMessageObject(val roomId: String, val content: String)
}