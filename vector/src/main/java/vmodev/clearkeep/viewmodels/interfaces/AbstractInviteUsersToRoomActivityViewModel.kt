package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractInviteUsersToRoomActivityViewModel : ViewModel() {
    abstract fun getUsers(): LiveData<Resource<List<User>>>;
    abstract fun getInviteUsersToRoomResult(): LiveData<Resource<Room>>;
    abstract fun setInviteUsersToRoom(roomId: String, userIds: List<String>);
    abstract fun setQuery(query: String);
    abstract fun setJoinRoom(roomId: String);
    abstract fun joinRoomResult() : LiveData<Resource<Room>>
    abstract fun getListUserSuggested(type:Int, userID:String):LiveData<Resource<List<User>>>
}