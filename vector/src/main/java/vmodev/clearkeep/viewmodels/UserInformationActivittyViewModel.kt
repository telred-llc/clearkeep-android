package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.RoomUserJoinRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserInformationActivityViewModel
import javax.inject.Inject

class UserInformationActivityViewModel @Inject constructor(userRepository: UserRepository, roomUserJoinRepository: RoomUserJoinRepository, roomRepository: RoomRepository) : AbstractUserInformationActivityViewModel() {

    private val _userId = MutableLiveData<String>();
    private val _roomIdForLeave = MutableLiveData<String>();
    private val _roomIdForAddToFavourite = MutableLiveData<String>();
    private val _roomIdForRemoveFromFavourite = MutableLiveData<String>();
    private val _roomIdForJoinRoom = MutableLiveData<String>();
    private val _userIdForCreateNewConversation = MutableLiveData<String>();
    private val _userByIdResult = Transformations.switchMap(_userId) { input -> userRepository.loadUser(input) }
    private val _directChatRoomsResult = Transformations.switchMap(_userId) { input -> roomUserJoinRepository.getListRoomListUserWithFilterAndUserId(input, arrayOf(1, 65, 129)) }
    private val _roomChatRoomsResult = Transformations.switchMap(_userId) { input -> roomUserJoinRepository.getListRoomListUserWithFilterAndUserId(input, arrayOf(2, 66, 130)) }
    private val _leaveRoomWithIdResult = Transformations.switchMap(_roomIdForLeave) { input -> roomRepository.leaveRoom(input) }
    private val _addRoomToFavouriteResult = Transformations.switchMap(_roomIdForAddToFavourite) { input -> roomRepository.addToFavourite(input) }
    private val _removeRoomFromFavouriteResult = Transformations.switchMap(_roomIdForRemoveFromFavourite) { input -> roomRepository.removeFromFavourite(input) }
    private val _joinRoomResult = Transformations.switchMap(_roomIdForJoinRoom) { input -> roomRepository.joinRoom(input) }
    private val _createNewConversationResult = Transformations.switchMap(_userIdForCreateNewConversation) { input -> roomRepository.createDirectChatRoom(input) }
    private val _setChangeNotificationState = MutableLiveData<ChangeNotificationStateObject>();
    private val _changeNotificationStateResult = Transformations.switchMap(_setChangeNotificationState) { input -> roomRepository.changeNotificationState(input.roomId, input.state) }

    override fun setUserId(userId: String) {
        _userId.value = userId;
    }

    override fun getUserByIdResult(): LiveData<Resource<User>> {
        return _userByIdResult;
    }

    override fun getDirectChatByUserIdResult(): LiveData<Resource<List<RoomListUser>>> {
        return _directChatRoomsResult;
    }

    override fun getRoomChatByUserIdResult(): LiveData<Resource<List<RoomListUser>>> {
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

    override fun gerRemoveRoomFromFavouriteResult(): LiveData<Resource<Room>> {
        return _removeRoomFromFavouriteResult;
    }

    override fun setUserIdForCreateNewConversation(userId: String) {
        _userIdForCreateNewConversation.value = userId;
    }

    override fun getCreateNewConversationResult(): LiveData<Resource<Room>> {
        return _createNewConversationResult;
    }

    override fun setChangeNotificationState(roomId: String, state: Byte) {
        _setChangeNotificationState.value = ChangeNotificationStateObject(roomId, state);
    }

    override fun getChangeNotificationStateResult(): LiveData<Resource<Room>> {
        return _changeNotificationStateResult;
    }
}
