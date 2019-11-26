package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractCallHistoryViewModel : ViewModel() {
    abstract fun getListCallHistory(decryptListMessage: List<MessageRoomUser>): LiveData<Resource<List<MessageRoomUser>>>
    abstract fun getListMessageRoomUser(): LiveData<Resource<List<MessageRoomUser>>>
    abstract fun setTimeForRefreshLoadMessage(time: Long);

}