package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomUserJoin
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractSplashActivityViewModel : ViewModel() {
    abstract fun getAllRoomResult(filters: Array<Int>): LiveData<Resource<List<Room>>>;
    abstract fun getUpdateUserResult(roomId: String): LiveData<Resource<List<User>>>
    abstract fun getUpdateRoomUserJoinResult(roomId: String, userId: String);
}