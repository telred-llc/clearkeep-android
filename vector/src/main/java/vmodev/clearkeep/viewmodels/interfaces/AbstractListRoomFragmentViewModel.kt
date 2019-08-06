package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.*

abstract class AbstractListRoomFragmentViewModel : ViewModel() {
    abstract fun setFiltersDirectRoom(filters: Array<Int>);
    abstract fun setFiltersGroupRoom(filters: Array<Int>)

    abstract fun getListDirectRoomResult(): LiveData<Resource<List<Room>>>
    abstract fun getListGroupRoomResult(): LiveData<Resource<List<Room>>>
    abstract fun setLeaveRoomId(roomId: String);
    abstract fun getLeaveRoomWithIdResult(): LiveData<Resource<String>>;
    abstract fun setAddToFavouriteRoomId(roomId: String);
    abstract fun getAddToFavouriteResult(): LiveData<Resource<Room>>;
    abstract fun setRoomIdForJoinRoom(roomId: String);
    abstract fun joinRoomWithIdResult(): LiveData<Resource<Room>>;
    abstract fun setRoomIdForRemoveFromFavourite(roomId: String);
    abstract fun gerRemoveRoomFromFavouriteResult(): LiveData<Resource<Room>>;
    abstract fun setIdForUpdateRoomNotify(roomId: String);
    abstract fun getUpdateRoomNotifyResult(): LiveData<Resource<Room>>;
    abstract fun setChangeNotificationState(roomId: String, state: Byte);
    abstract fun getChangeNotificationStateResult(): LiveData<Resource<Room>>;
    abstract fun getRoomUserJoinResult(userIds : Array<String>): LiveData<Resource<List<User>>>;
    abstract fun getListRoomListUserDirectResult(): LiveData<Resource<List<RoomListUser>>>
    abstract fun getListRoomListUserGroup() : LiveData<Resource<List<RoomListUser>>>

    data class ChangeNotificationStateObject(val roomId: String, val state: Byte)
}