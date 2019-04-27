package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import javax.inject.Inject

class RoomViewModel @Inject constructor(roomRepository: RoomRepository) : AbstractRoomViewModel() {
    private val _filters = MutableLiveData<Array<Int>>();
    private val _id = MutableLiveData<String>();
    private val _joinRoom = MutableLiveData<String>();
    private val _leaveRoom = MutableLiveData<String>();
    private val _otherUserId = MutableLiveData<String>();
    private val _createNewRoom = MutableLiveData<RoomRepository.CreateNewRoomObject>();
    private val _inviteUserToRoom = MutableLiveData<RoomRepository.InviteUsersToRoomObject>();

    private val rooms: LiveData<Resource<List<Room>>> = Transformations.switchMap(_filters) { input ->
        roomRepository.loadListRoom(input);
    }
    private val roomFind: LiveData<Resource<Room>> = Transformations.switchMap(_id) { input -> roomRepository.loadRoom(input) }
    private val roomJoin: LiveData<Resource<Room>> = Transformations.switchMap(_joinRoom) { input -> roomRepository.joinRoom(input) }
    private val roomLeave: LiveData<Resource<String>> = Transformations.switchMap(_leaveRoom) { input -> roomRepository.leaveRoom(input) }
    private val inviteUser: LiveData<Resource<Room>> = Transformations.switchMap(_otherUserId) { input ->
        roomRepository.createDirectChatRoom(input);
    }
    private val createNewRoom: LiveData<Resource<Room>> = Transformations.switchMap(_createNewRoom) { input ->
        roomRepository.createNewRoom(input)
    }
    private val inviteUsersToRoom: LiveData<Resource<Room>> = Transformations.switchMap(_inviteUserToRoom) { input ->
        roomRepository.inviteUsersToRoom(input);
    }

    private val roomMerge = MediatorLiveData<Resource<Room>>();

    init {
        roomMerge.addSource(roomFind) { t -> roomMerge.value = t }
        roomMerge.addSource(roomJoin) { t -> roomMerge.value = t }
    }

    override fun setFilter(filters: Array<Int>) {
        _filters.value = filters;
    }

    override fun getRoomsData(): LiveData<Resource<List<Room>>> {
        return rooms;
    }

    override fun getFilterData(): LiveData<Array<Int>> {
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
        _leaveRoom.value = id;
    }

    override fun getLeaveRoom(): LiveData<Resource<String>> {
        return roomLeave;
    }
}