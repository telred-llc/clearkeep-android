package vmodev.clearkeep.viewmodels

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.RoomUserJoinRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractInviteUsersToRoomActivityViewModel
import javax.inject.Inject

class InviteUsersToRoomActivityViewModel @Inject constructor(userRepository: UserRepository, roomRepository: RoomRepository, private val roomUserJoinRepository: RoomUserJoinRepository) : AbstractInviteUsersToRoomActivityViewModel() {

    private val _query = MutableLiveData<String>()
    private val _inviteUserToRoom = MutableLiveData<RoomRepository.InviteUsersToRoomObject>()
    private val _joinRoom = MutableLiveData<String>()
    private val users: LiveData<Resource<List<User>>> = Transformations.switchMap(_query) { input ->
        userRepository.findUserFromNetwork(input)
    }

    private val inviteUsersToRoom: LiveData<Resource<Room>> = Transformations.switchMap(_inviteUserToRoom) { input ->
        roomRepository.inviteUsersToRoom(input)
    }

    private val _joinRoomResult = Transformations.switchMap(_joinRoom) { input -> roomRepository.joinRoom(input) }

    override fun getUsers(): LiveData<Resource<List<User>>> {
        return users
    }

    override fun getInviteUsersToRoomResult(): LiveData<Resource<Room>> {
        return inviteUsersToRoom
    }

    override fun setInviteUsersToRoom(roomId: String, userIds: List<String>) {
        _inviteUserToRoom.value = RoomRepository.InviteUsersToRoomObject(roomId, userIds)
    }

    override fun setQuery(query: String) {
        if (!TextUtils.equals(_query.value, query)) {
            _query.value = query
        }
    }

    override fun setJoinRoom(roomId: String) {
        if (!TextUtils.equals(_joinRoom.value, roomId))
            _joinRoom.value = roomId
    }

    override fun joinRoomResult(): LiveData<Resource<Room>> {
        return _joinRoomResult
    }

    override fun getListUserSuggested(type: Int, userID: String): LiveData<Resource<List<User>>> {
        return roomUserJoinRepository.getListUserSuggested(type, userID)
    }
}