package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.RoomViewModel

abstract class AbstractRoomViewModel : ViewModel() {
    abstract fun setFilter(filters: Array<Int>, loadType : Int = 0);
    abstract fun setRoomId(id: String);
    abstract fun getRoomsData(): LiveData<Resource<List<Room>>>
    abstract fun getFilterData(): LiveData<RoomViewModel.RoomFilters>;
    abstract fun getRoom(): LiveData<Resource<Room>>;
    abstract fun joinRoom(id: String);
    abstract fun setLeaveRoom(id: String);
    abstract fun getLeaveRoom(): LiveData<Resource<String>>;
    abstract fun setInviteUserToDirectChat(id: String);
    abstract fun getInviteUserToDirectChat(): LiveData<Resource<Room>>;
    abstract fun createNewRoom(): LiveData<Resource<Room>>;
    abstract fun setCreateNewRoom(name: String, topic: String, visibility: String);
    abstract fun setInviteUsersToRoom(roomId: String, userIds: List<String>);
    abstract fun getInviteUsersToRoomResult(): LiveData<Resource<Room>>;
    abstract fun setTextForFindByText(keyword: String);
    abstract fun getFindByTextResult(): LiveData<Resource<List<String>>>;
    abstract fun setAddToFavourite(roomId: String);
    abstract fun getAddToFavouriteResult(): LiveData<Resource<Room>>
    abstract fun setRemoveFromFavourite(roomId: String);
    abstract fun getRemoveFromFavouriteResult(): LiveData<Resource<Room>>
    abstract fun getGetUserFromRoomIdResult() : LiveData<Resource<List<User>>>;
    abstract fun setGetUserFromRoomId(roomId: String);
    abstract fun getRoomListUserFindByID(roomId: String):LiveData<Resource<RoomListUser>>
}