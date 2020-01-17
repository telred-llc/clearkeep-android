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
import vmodev.clearkeep.viewmodels.interfaces.AbstractFindAndCreateNewConversationActivityViewModel
import javax.inject.Inject

class FindAndCreateNewConversationActivityViewModel @Inject constructor(private val userRepository: UserRepository, private val roomRepository: RoomRepository, private val roomUserJoinRepository: RoomUserJoinRepository) : AbstractFindAndCreateNewConversationActivityViewModel() {

    private val _query = MutableLiveData<String>()
    private val _otherUserId = MutableLiveData<String>()
    private val _joinRoom = MutableLiveData<String>()
    private val roomJoin: LiveData<Resource<Room>> = Transformations.switchMap(_joinRoom) { input -> roomRepository.joinRoom(input) }
    private val users: LiveData<Resource<List<User>>> = Transformations.switchMap(_query) { input ->
        userRepository.findUserFromNetwork(input)
    }
    private val inviteUser: LiveData<Resource<Room>> = Transformations.switchMap(_otherUserId) { input ->
        roomRepository.createDirectChatRoom(input)
    }

    override fun getUsers(): LiveData<Resource<List<User>>> {
        return users
    }

    override fun setInviteUserToDirectChat(userId: String) {
        if (!TextUtils.equals(_otherUserId.value, userId))
            _otherUserId.value = userId
    }

    override fun getInviteUserToDirectChat(): LiveData<Resource<Room>> {
        return inviteUser
    }

    override fun setQuery(query: String) {
        if (!TextUtils.equals(_query.value, query))
            _query.value = query
    }


    override fun joinRoomResult(): LiveData<Resource<Room>> {
        return roomJoin
    }

    override fun setJoinRoom(roomId: String) {
        if (!TextUtils.equals(_joinRoom.value, roomId))
            _joinRoom.value = roomId
    }

    override fun getListUserSuggested(type: Int, userID: String): LiveData<Resource<List<User>>> {
        return roomUserJoinRepository.getListUserSuggested(type, userID)
    }

    override fun getRoomByID(roomId: String): LiveData<Resource<Room>> {
        return roomRepository.getRoomByUserId(roomId)
    }

}