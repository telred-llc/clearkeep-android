package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room

abstract class AbstractMessageListActivityViewModel : ViewModel() {
    abstract fun getListMessageResult(): LiveData<Resource<List<Message>>>;
    abstract fun setRoomIdForGetListMessage(roomId: String);
    abstract fun setMessageId(id: String);
    abstract fun getMessageResult(): LiveData<Resource<Message>>;
    abstract fun getRoomResult(): LiveData<Resource<Room>>;
    abstract fun removeMatrixMessageHandler(roomId: String);
    abstract fun registerMatrixMessageHandlerResult(): LiveData<Resource<List<Message>>>;
}