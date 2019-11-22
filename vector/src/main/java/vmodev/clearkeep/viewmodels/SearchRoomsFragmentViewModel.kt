package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.RoomUserJoinRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchRoomsFragmentViewModel
import javax.inject.Inject

class SearchRoomsFragmentViewModel @Inject constructor(roomRepository: RoomRepository, private val roomUserJoinRepository: RoomUserJoinRepository,userRepository: UserRepository) : AbstractSearchRoomsFragmentViewModel() {

    private val _roomIdForLeave = MutableLiveData<String>();
    private val _roomIdForJoinRoom = MutableLiveData<String>();
    private val _query = MutableLiveData<String>();
    private val _searchRoomInviteResult = Transformations.switchMap(_query) { input -> roomUserJoinRepository.getListRoomListUserWithListRoomId(listOf(65, 66), input) }
    private val _joinRoomResult = Transformations.switchMap(_roomIdForJoinRoom) { input -> roomRepository.joinRoom(input) }
    private val _leaveRoomWithIdResult = Transformations.switchMap(_roomIdForLeave) { input -> roomRepository.leaveRoom(input) }
    private val _searchRoomNormalResult = Transformations.switchMap(_query) { input -> roomUserJoinRepository.getListRoomListUserWithListRoomId(listOf(2), input) }
//    private val _searchRoomDirectoryResult = Transformations.switchMap(_query) { input -> roomUserJoinRepository.getListRoomDirectoryWithListRoomId(listOf(2),0, input) }
    private val _searchDirectRoomNormalResult = Transformations.switchMap(_query) { input -> roomUserJoinRepository.getListRoomListUserWithListRoomId(listOf(1), input) }

    private val _getListRoomDirectory = Transformations.switchMap(_query) { input -> roomUserJoinRepository.getListRoomDirectory(20, input) }

    override fun setQueryForSearch(query: String) {
        if (_query.value != query)
            _query.value = query;
    }

    override fun getRoomInviteSearchResult(): LiveData<Resource<List<RoomListUser>>> {
        return _searchRoomInviteResult;
    }

//    override fun getRoomFavouriteSearchResult(): LiveData<Resource<List<RoomListUser>>> {
//        return _searchRoomFavouriteResult;
//    }

    override fun getRoomNormalSearchResult(): LiveData<Resource<List<RoomListUser>>> {
        return _searchRoomNormalResult;
    }

//    override fun getRoomDirectorySearchResult(): LiveData<Resource<List<RoomListUser>>> {
//        return _searchRoomDirectoryResult
//    }

    override fun getDirectRoomNormalSearchResult(): LiveData<Resource<List<RoomListUser>>> {
        return _searchDirectRoomNormalResult;
    }

    override fun joinRoomWithIdResult(): LiveData<Resource<Room>> {
        return _joinRoomResult;
    }

    override fun setRoomIdForJoinRoom(roomId: String) {
        _roomIdForJoinRoom.value = roomId;
    }

    override fun getLeaveRoomWithIdResult(): LiveData<Resource<String>> {
        return _leaveRoomWithIdResult;
    }

    override fun setLeaveRoomId(roomId: String) {
        _roomIdForLeave.value = roomId;
    }
    override fun getListRoomDirectory(): LiveData<Resource<List<PublicRoom>>>  {
      return _getListRoomDirectory;
    }

}