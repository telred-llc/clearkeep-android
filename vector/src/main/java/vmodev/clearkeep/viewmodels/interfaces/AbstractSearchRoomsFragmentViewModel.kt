package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractSearchRoomsFragmentViewModel : ViewModel() {
    abstract fun setQueryForSearch(query: String);
    abstract fun getRoomInviteSearchResult(): LiveData<Resource<List<RoomListUser>>>
    abstract fun joinRoomWithIdResult(): LiveData<Resource<Room>>;
    abstract fun getRoomNormalSearchResult(): LiveData<Resource<List<RoomListUser>>>
//    abstract fun getRoomDirectorySearchResult(): LiveData<Resource<List<RoomListUser>>>
    abstract fun getDirectRoomNormalSearchResult(): LiveData<Resource<List<RoomListUser>>>
    abstract fun setRoomIdForJoinRoom(roomId: String);
    abstract fun getLeaveRoomWithIdResult(): LiveData<Resource<String>>;
    abstract fun setLeaveRoomId(roomId: String);
    abstract fun getListRoomDirectory():LiveData<Resource<List<PublicRoom>>>
}