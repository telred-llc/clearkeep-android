package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.RoomUserJoinRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.*
import vmodev.clearkeep.viewmodels.interfaces.AbstractListRoomFragmentViewModel
import javax.inject.Inject

class ListRoomFragmentViewModel @Inject constructor(roomRepository: RoomRepository, private val userRepository: UserRepository, private val roomUserJoinRepository: RoomUserJoinRepository) : AbstractListRoomFragmentViewModel() {

    private val _directRoomFilters = MutableLiveData<Array<Int>>();
    private val _groupRoomFilters = MutableLiveData<Array<Int>>();
    private val _favouriteFilters = MutableLiveData<Array<Int>>();
    private val _roomIdForLeave = MutableLiveData<String>();
    private val _roomIdForAddToFavourite = MutableLiveData<String>();
    private val _roomIdForRemoveFromFavourite = MutableLiveData<String>();
    private val _roomIdForJoinRoom = MutableLiveData<String>();
    private val _roomIdForUpdateNotify = MutableLiveData<String>();
    private val _setChangeNotificationState = MutableLiveData<ChangeNotificationStateObject>();
    private val _removeFromFavourite = MutableLiveData<String>();

    private val _leaveRoomWithIdResult = Transformations.switchMap(_roomIdForLeave) { input -> roomRepository.leaveRoom(input) }
    private val _addRoomToFavouriteResult = Transformations.switchMap(_roomIdForAddToFavourite) { input -> roomRepository.addToFavourite(input) }
    private val _removeRoomFromFavouriteResult = Transformations.switchMap(_roomIdForRemoveFromFavourite) { input -> roomRepository.removeFromFavourite(input) }
    private val _joinRoomResult = Transformations.switchMap(_roomIdForJoinRoom) { input -> roomRepository.joinRoom(input) }
    private val _getListFavouritesResult = Transformations.switchMap(_favouriteFilters) { input -> roomRepository.loadListRoom(input) }
    private val _getListDirectRoomResult = Transformations.switchMap(_directRoomFilters) { input ->
        roomRepository.loadListRoom(input)
    }
    private val _getListGroupRoomResult = Transformations.switchMap(_groupRoomFilters) { input -> roomRepository.loadListRoom(input) }
    private val _updateRoomNotifyResult = Transformations.switchMap(_roomIdForUpdateNotify) { input -> roomRepository.setRoomNotify(input) }
    private val _changeNotificationStateResult = Transformations.switchMap(_setChangeNotificationState) { input -> roomRepository.changeNotificationState(input.roomId, input.state) }
    private val removeFromFavouriteResult = Transformations.switchMap(_removeFromFavourite) { input -> roomRepository.removeFromFavourite(input) }
    private val _listRoomListUserGroupResult = Transformations.switchMap(_groupRoomFilters) { input -> roomUserJoinRepository.getRoomListUser(input) }
    private val _listRoomListUserDirectResult = Transformations.switchMap(_directRoomFilters) { input -> roomUserJoinRepository.getRoomListUser(input) }
    private val _listRoomListUserFavouritesResult = Transformations.switchMap(_favouriteFilters) { input -> roomUserJoinRepository.getRoomListUser(input) }


    override fun setFiltersDirectRoom(filters: Array<Int>) {
        _directRoomFilters.value = filters;
    }

    override fun setFiltersGroupRoom(filters: Array<Int>) {
        _groupRoomFilters.value = filters;
    }

    override fun getListDirectRoomResult(): LiveData<Resource<List<RoomListUser>>> {
        return _listRoomListUserDirectResult;
    }

    override fun getListGroupRoomResult(): LiveData<Resource<List<RoomListUser>>> {
        return _listRoomListUserGroupResult;
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

    override fun setIdForUpdateRoomNotify(roomId: String) {
        _roomIdForUpdateNotify.value = roomId;
    }

    override fun getUpdateRoomNotifyResult(): LiveData<Resource<Room>> {
        return _updateRoomNotifyResult;
    }

    override fun setChangeNotificationState(roomId: String, state: Byte) {
        _setChangeNotificationState.value = ChangeNotificationStateObject(roomId, state);
    }

    override fun getChangeNotificationStateResult(): LiveData<Resource<Room>> {
        return _changeNotificationStateResult;
    }

    override fun getRemoveFromFavouriteResult(): LiveData<Resource<Room>> {
        return removeFromFavouriteResult;
    }

    override fun setRemoveFromFavourite(roomId: String) {
        _removeFromFavourite.value = roomId;
    }

    override fun setFiltersFavouriteRoom(filters: Array<Int>) {
        _favouriteFilters.value = filters;
    }

    override fun getListFavouritesResult(): LiveData<Resource<List<RoomListUser>>> {
        return _listRoomListUserFavouritesResult;
    }

    override fun getRoomUserJoinResult(userIds: Array<String>): LiveData<Resource<List<User>>> {
        return userRepository.getUsersWithId(userIds);
    }

    override fun getListRoomListUserDirectResult(): LiveData<Resource<List<RoomListUser>>> {
        return _listRoomListUserDirectResult;
    }

    override fun getListRoomListUserGroup(): LiveData<Resource<List<RoomListUser>>> {
        return _listRoomListUserGroupResult;
    }
}