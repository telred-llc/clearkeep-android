package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractRoomSettingsActivityViewModel : ViewModel() {
    abstract fun setRoomId(roomId : String);
    abstract fun getRoom() : LiveData<Resource<Room>>;
    abstract fun setLeaveRoom(roomId: String);
    abstract fun getLeaveRoom()  : LiveData<Resource<String>>;
    abstract fun setUserId(userId : String);
    abstract fun getUserResult() : LiveData<Resource<User>>;
}