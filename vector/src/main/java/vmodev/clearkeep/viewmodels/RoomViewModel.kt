package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.RoomUserJoinRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import javax.inject.Inject

class RoomViewModel @Inject constructor(roomRepository: RoomRepository, roomUserJoinRepository: RoomUserJoinRepository, private val userRepository: UserRepository) : AbstractRoomViewModel() {
    private val _filters = MutableLiveData<RoomFilters>();
    private val _id = MutableLiveData<String>();
    private val _joinRoom = MutableLiveData<String>();
    private val _leaveRoom = MutableLiveData<String>();
    private val _otherUserId = MutableLiveData<String>();
    private val _createNewRoom = MutableLiveData<RoomRepository.CreateNewRoomObject>();
    private val _inviteUserToRoom = MutableLiveData<RoomRepository.InviteUsersToRoomObject>();
    private val _findByText = MutableLiveData<String>();
    private val _addToFavourite = MutableLiveData<String>();
    private val _removeFromFavourite = MutableLiveData<String>();
    private val _roomIdForGetUser = MutableLiveData<String>();

    private val rooms: LiveData<Resource<List<Room>>> = Transformations.switchMap(_filters) { input ->
        roomRepository.loadListRoom(input.filters, input.loadType);
    }
    private val roomFind: LiveData<Resource<Room>> = Transformations.switchMap(_id) { input -> roomRepository.loadRoom(input) }
    private val roomJoin: LiveData<Resource<Room>> = Transformations.switchMap(_joinRoom) { input -> roomRepository.joinRoom(input) }
    private val roomLeave: LiveData<Resource<String>> = Transformations.switchMap(_leaveRoom) { input -> roomRepository.leaveRoom(input) }
    private val inviteUser: LiveData<Resource<Room>> = Transformations.switchMap(_otherUserId) { input ->
        roomRepository.createDirectChatRoom(input);
    }
    private val createNewRoom: LiveData<Resource<Room>> = Transformations.switchMap(_createNewRoom) { input ->
        roomRepository.createNewRoom(input.name, input.topic, input.visibility)
    }
    private val inviteUsersToRoom: LiveData<Resource<Room>> = Transformations.switchMap(_inviteUserToRoom) { input ->
        roomRepository.inviteUsersToRoom(input);
    }
    private val findBytTextResult: LiveData<Resource<List<String>>> = Transformations.switchMap(_findByText) { input -> roomRepository.findListRoomWithText(input) }

    private val addToFavourite: LiveData<Resource<Room>> = Transformations.switchMap(_addToFavourite) { input ->
        roomRepository.addToFavourite(input);
    }
    private val removeFromFavourite: LiveData<Resource<Room>> = Transformations.switchMap(_removeFromFavourite) { input ->
        roomRepository.removeFromFavourite(input);
    }
    private val usersFromRoomId: LiveData<Resource<List<User>>> = Transformations.switchMap(_roomIdForGetUser) { input ->
        userRepository.getListUserInRoomFromNetwork(input);
    }

    private val roomMerge = MediatorLiveData<Resource<Room>>();

    init {
        roomMerge.addSource(roomFind) { t -> roomMerge.value = t }
        roomMerge.addSource(roomJoin) { t -> roomMerge.value = t }
    }

    override fun setFilter(filters: Array<Int>, loadType: Int) {
        _filters.value = RoomFilters(filters, loadType);
    }

    override fun getRoomsData(): LiveData<Resource<List<Room>>> {
        return rooms;
    }

    override fun getFilterData(): LiveData<RoomFilters> {
        return _filters;
    }

    override fun getRoom(): LiveData<Resource<Room>> {
        return roomMerge;
    }

    override fun setRoomId(id: String) {
        if (_id.value != id)
            _id.value = id;
    }

    override fun joinRoom(id: String) {
        if (_joinRoom.value != id)
            _joinRoom.value = id;
    }

    override fun getInviteUserToDirectChat(): LiveData<Resource<Room>> {
        return inviteUser;
    }

    override fun setInviteUserToDirectChat(id: String) {
        if (_otherUserId.value != id)
            _otherUserId.value = id;
    }

    override fun createNewRoom(): LiveData<Resource<Room>> {
        return createNewRoom;
    }

    override fun setCreateNewRoom(name: String, topic: String, visibility: String) {
        _createNewRoom.value = RoomRepository.CreateNewRoomObject(name, topic, visibility);
    }

    override fun setInviteUsersToRoom(roomId: String, userIds: List<String>) {
        _inviteUserToRoom.value = RoomRepository.InviteUsersToRoomObject(roomId, userIds);
    }

    override fun getInviteUsersToRoomResult(): LiveData<Resource<Room>> {
        return inviteUsersToRoom;
    }

    override fun setLeaveRoom(id: String) {
        if (_leaveRoom.value != id)
            _leaveRoom.value = id;
    }

    override fun getLeaveRoom(): LiveData<Resource<String>> {
        return roomLeave;
    }

    override fun setTextForFindByText(keyword: String) {
        if (_findByText.value != keyword)
            _findByText.value = keyword;
    }

    override fun getFindByTextResult(): LiveData<Resource<List<String>>> {
        return findBytTextResult;
    }

    override fun setAddToFavourite(roomId: String) {
        _addToFavourite.value = roomId;
    }

    override fun getAddToFavouriteResult(): LiveData<Resource<Room>> {
        return addToFavourite;
    }

    override fun setRemoveFromFavourite(roomId: String) {
        _removeFromFavourite.value = roomId;
    }

    override fun getRemoveFromFavouriteResult(): LiveData<Resource<Room>> {
        return removeFromFavourite;
    }

    override fun getGetUserFromRoomIdResult(): LiveData<Resource<List<User>>> {
        return usersFromRoomId;
    }

    override fun setGetUserFromRoomId(roomId: String) {
        _roomIdForGetUser.value = roomId;
    }

    class RoomFilters constructor(val filters: Array<Int>, val loadType: Int = 0)
}