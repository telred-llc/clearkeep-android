package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractContactFragmentViewModel : ViewModel() {
    abstract fun getListRoomByType(): LiveData<Resource<List<RoomListUser>>>;
    abstract fun setListType(types: Array<Int>);
    abstract fun setIdForUpdateRoomNotify(roomId: String);
    abstract fun getUpdateRoomNotifyResult(): LiveData<Resource<Room>>;
    abstract fun getRoomUserJoinResult(userIds: Array<String>): LiveData<Resource<List<User>>>;
}