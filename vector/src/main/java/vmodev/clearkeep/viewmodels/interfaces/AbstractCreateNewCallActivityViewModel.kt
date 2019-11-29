package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room

abstract class AbstractCreateNewCallActivityViewModel : AbstractInviteUsersToRoomActivityViewModel() {
    abstract fun setCreateNewRoom(name: String, topic: String, visibility: String)
    abstract fun getCreateNewRoomResult(): LiveData<Resource<Room>>
    data class CreateNewRoom(val name: String, val topic: String, val visibility: String)
}