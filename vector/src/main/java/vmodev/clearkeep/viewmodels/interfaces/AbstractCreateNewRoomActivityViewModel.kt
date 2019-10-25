package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractCreateNewRoomActivityViewModel : ViewModel() {
    abstract fun setCreateNewRoom(name: String, topic: String, visibility: String);
    abstract fun createNewRoomResult(): LiveData<Resource<Room>>;
    abstract fun getListUserSuggested(type:Int, userID:String):LiveData<Resource<List<User>>>
    data class CreateNewRoomObject(val name: String, val topic: String, val visibility: String);
    abstract fun setInviteUsersToRoom(roomId: String, userIds: List<String>);


}