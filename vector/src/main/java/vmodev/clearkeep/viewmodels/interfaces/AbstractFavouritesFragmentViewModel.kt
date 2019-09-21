package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractFavouritesFragmentViewModel : ViewModel() {
    abstract fun getListTypeFavouritesDirectResult(): LiveData<Resource<List<Room>>>;
    abstract fun setListTypeFavouritesDirect(types: Array<Int>);
    abstract fun getRemoveFromFavouriteResult(): LiveData<Resource<Room>>;
    abstract fun setRemoveFromFavourite(roomId: String);
    abstract fun getLeaveRoom(): LiveData<Resource<String>>;
    abstract fun setLeaveRoom(roomId: String);
    abstract fun setListTypeFavouritesGroup(types: Array<Int>)
    abstract fun getListTypeFavouritesGroupResult(): LiveData<Resource<List<Room>>>
    abstract fun setIdForUpdateRoomNotify(roomId: String);
    abstract fun getUpdateRoomNotifyResult(): LiveData<Resource<Room>>;
    abstract fun setChangeNotificationState(roomId: String, state: Byte);
    abstract fun getChangeNotificationStateResult(): LiveData<Resource<Room>>;
    abstract fun getRoomUserJoinResult(userIds: Array<String>): LiveData<Resource<List<User>>>;
    abstract fun getListRoomListUserResult() : LiveData<Resource<List<RoomListUser>>>

    data class ChangeNotificationStateObject(val roomId: String, val state: Byte)
}