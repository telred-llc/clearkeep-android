package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room

abstract class AbstractRoomViewModel : ViewModel() {
    abstract fun setFilter(filters: Array<Int>);
    abstract fun setRoomId(id: String);
    abstract fun getRoomsData(): LiveData<Resource<List<Room>>>
    abstract fun getFilterData(): LiveData<Array<Int>>;
    abstract fun getRoom(): LiveData<Resource<Room>>;
    abstract fun joinRoom(id: String);
    abstract fun setLeaveRoom(id : String);
    abstract fun getLeaveRoom() : LiveData<Resource<String>>;
    abstract fun setInviteUserToDirectChat(id: String);
    abstract fun getInviteUserToDirectChat(): LiveData<Resource<Room>>;
    abstract fun createNewRoom(): LiveData<Resource<Room>>;
    abstract fun setCreateNewRoom(name: String, topic: String, visibility: String);
    abstract fun setInviteUsersToRoom(roomId: String, userIds: List<String>);
    abstract fun getInviteUsersToRoomResult() : LiveData<Resource<Room>>;
}