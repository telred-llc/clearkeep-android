package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractMessageListActivityViewModel : ViewModel() {
    abstract fun getListMessageResult(): LiveData<Resource<List<Message>>>;
    abstract fun setRoomIdForGetListMessage(roomId: String);
}