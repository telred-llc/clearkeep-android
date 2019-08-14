package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractSearchFilesFragmentViewModel : ViewModel() {
    abstract fun setTimeForRefreshLoadMessage(time: Long);
    abstract fun getLoadMessagesResult(): LiveData<Resource<List<Message>>>
    abstract fun getListMessageRoomUser(): LiveData<Resource<List<MessageRoomUser>>>
    abstract fun decryptListMessage(messages: List<MessageRoomUser>): LiveData<Resource<List<MessageRoomUser>>>;
}