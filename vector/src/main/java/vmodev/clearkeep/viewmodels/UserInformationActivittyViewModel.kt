package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.RoomUserJoinRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserInformationActivityViewModel
import javax.inject.Inject

class UserInformationActivityViewModel @Inject constructor(userRepository: UserRepository, roomUserJoinRepository: RoomUserJoinRepository, roomRepository: RoomRepository) : AbstractUserInformationActivityViewModel() {

    private val _userId = MutableLiveData<String>();
    private val _roomIdForLeave = MutableLiveData<String>();
    private val _roomIdForAddToFavourite = MutableLiveData<String>();
    private val _roomIdForRemoveFromFavourite = MutableLiveData<String>();
    private val _roomIdForJoinRoom = MutableLiveData<String>();
    private val _userByIdResult = Transformations.switchMap(_userId) { input -> userRepository.loadUser(input) }
    private val _directChatRoomsResult = Transformations.switchMap(_userId) { input -> roomUserJoinRepository.getDirectChatRoomByUserId(input) }
    private val _roomChatRoomsResult = Transformations.switchMap(_userId) { input -> roomUserJoinRepository.getRoomChatRoomByUserId(input) }
    private val _leaveRoomWithIdResult = Transformations.switchMap(_roomIdForLeave) { input -> roomRepository.leaveRoom(input) }
    private val _addRoomToFavouriteResult = Transformations.switchMap(_roomIdForAddToFavourite) { input -> roomRepository.addToFavourite(input) }
    private val _removeRoomFromFavouriteResult = Transformations.switchMap(_roomIdForRemoveFromFavourite) { input -> roomRepository.removeFromFavourite(input) }
    private val _joinRoomResult = Transformations.switchMap(_roomIdForJoinRoom) { input -> roomRepository.joinRoom(input) }

    override fun setUserId(userId: String) {
        _userId.value = userId;
    }

    override fun getUserByIdResult(): LiveData<Resource<User>> {
        return _userByIdResult;
    }

    override fun getDirectChatByUserIdResult(): LiveData<Resource<List<Room>>> {
        return _directChatRoomsResult;
    }

    override fun getRoomChatByUserIdResult(): LiveData<Resource<List<Room>>> {
        return _roomChatRoomsResult;
    }

    override fun setLeaveRoomId(roomId: String) {
        _roomIdForLeave.value = roomId;
    }

    override fun getLeaveRoomWithIdResult(): LiveData<Resource<String>> {
        return _leaveRoomWithIdResult;
    }

    override fun setAddToFavouriteRoomId(roomId: String) {
        _roomIdForAddToFavourite.value = roomId;
    }

    override fun getAddToFavouriteResult(): LiveData<Resource<Room>> {
        return _addRoomToFavouriteResult;
    }

    override fun setRoomIdForJoinRoom(roomId: String) {
        _roomIdForJoinRoom.value = roomId;
    }

    override fun joinRoomWithIdResult(): LiveData<Resource<Room>> {
        return _joinRoomResult;
    }

    override fun setRoomIdForRemoveFromFavourite(roomId: String) {
        _roomIdForRemoveFromFavourite.value = roomId;
    }

    override fun gerRemoveRoomFromFavourtieResult(): LiveData<Resource<Room>> {
        return _removeRoomFromFavouriteResult;
    }
}
