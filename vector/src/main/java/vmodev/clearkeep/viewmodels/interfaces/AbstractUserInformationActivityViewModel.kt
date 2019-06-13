package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractUserInformationActivityViewModel : ViewModel() {
    abstract fun setUserId(userId: String);
    abstract fun getUserByIdResult(): LiveData<Resource<User>>;
    abstract fun getDirectChatByUserIdResult(): LiveData<Resource<List<Room>>>;
    abstract fun getRoomChatByUserIdResult(): LiveData<Resource<List<Room>>>;
    abstract fun setLeaveRoomId(roomId: String);
    abstract fun getLeaveRoomWithIdResult(): LiveData<Resource<String>>;
    abstract fun setAddToFavouriteRoomId(roomId: String);
    abstract fun getAddToFavouriteResult(): LiveData<Resource<Room>>;
    abstract fun setRoomIdForJoinRoom(roomId: String);
    abstract fun joinRoomWithIdResult(): LiveData<Resource<Room>>;
    abstract fun setRoomIdForRemoveFromFavourite(roomId: String);
    abstract fun gerRemoveRoomFromFavourtieResult(): LiveData<Resource<Room>>;
}