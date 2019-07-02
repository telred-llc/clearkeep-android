package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room

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
}