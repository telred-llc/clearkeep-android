package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomListUser

abstract class AbstractRoomShareFileFragmentViewModel:ViewModel(){
    abstract fun getListRoomByType(): LiveData<Resource<List<RoomListUser>>>;
    abstract fun setListType(types: Array<Int>);
    abstract fun getAddToFavouriteResult(): LiveData<Resource<Room>>;
    abstract fun setAddToFavourite(roomId: String);
    abstract fun getLeaveRoom(): LiveData<Resource<String>>;
    abstract fun setLeaveRoom(roomId: String);
    abstract fun setQueryForSearch(query: String);
    abstract fun getSearchResult(): LiveData<Resource<List<RoomListUser>>>;
}