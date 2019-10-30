package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.Completable
import io.reactivex.Observable
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomSettingsFragmentViewModel
import java.io.InputStream
import javax.inject.Inject

class RoomSettingsFragmentViewModel @Inject constructor(private val roomRepository: RoomRepository, userRepository: UserRepository) : AbstractRoomSettingsFragmentViewModel() {

    private val _setRoomId = MutableLiveData<String>();
    private val _setLeaveRoom = MutableLiveData<String>();
    private val _setUserId = MutableLiveData<String>();

    private val _getRoomResult = Transformations.switchMap(_setRoomId) { input -> roomRepository.loadRoom(input) }
    private val _getLeaveRoomResult = Transformations.switchMap(_setLeaveRoom) { input -> roomRepository.leaveRoom(input) }
    private val _getUserResult = Transformations.switchMap(_setUserId) { input -> userRepository.loadUser(input) }

    override fun setRoomId(roomId: String) {
        if (_setRoomId.value != roomId)
            _setRoomId.value = roomId;
    }

    override fun getRoom(): LiveData<Resource<Room>> {
        return _getRoomResult;
    }

    override fun setLeaveRoom(roomId: String) {
        if (_setLeaveRoom.value != roomId)
            _setLeaveRoom.value = roomId;
    }

    override fun getLeaveRoom(): LiveData<Resource<String>> {
        return _getLeaveRoomResult;
    }

    override fun setUserId(userId: String) {
        if (_setUserId.value != userId)
            _setUserId.value = userId;
    }

    override fun getUserResult(): LiveData<Resource<User>> {
        return _getUserResult;
    }

    override fun updateRoomName(roomId: String, roomName: String): Completable {
        return roomRepository.updateRoomNameToNetwork(roomId, roomName)
    }

    override fun updateRoomTopic(roomId: String, roomTopic: String): Completable {
        return roomRepository.updateRoomTopicToNetwork(roomId, roomTopic)
    }

    override fun updateRoomAvatar(roomId: String, avatar: InputStream): Completable {
        return roomRepository.updateRoomAvatarToNetwork(roomId, avatar)
    }
}