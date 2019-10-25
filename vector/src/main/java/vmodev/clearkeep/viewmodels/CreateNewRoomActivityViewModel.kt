package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.RoomUserJoinRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractCreateNewRoomActivityViewModel
import javax.inject.Inject

class CreateNewRoomActivityViewModel @Inject constructor(private val roomRepository: RoomRepository, private val userRepository: UserRepository, private val roomUserJoinRepository: RoomUserJoinRepository) : AbstractCreateNewRoomActivityViewModel() {
    private val _setCreateNewRoomObject = MutableLiveData<CreateNewRoomObject>();
    private val _createNewRoomResult = Transformations.switchMap(_setCreateNewRoomObject) { input -> roomRepository.createNewRoom(input.name, input.topic, input.visibility) }
    private val _getAllListUser = MutableLiveData<User>()
    private val _inviteUserToRoom = MutableLiveData<RoomRepository.InviteUsersToRoomObject>();

    override fun setCreateNewRoom(name: String, topic: String, visibility: String) {
        _setCreateNewRoomObject.value = CreateNewRoomObject(name, topic, visibility);
    }
    override fun createNewRoomResult(): LiveData<Resource<Room>> {
        return _createNewRoomResult;
    }
    override fun getListUserSuggested(type:Int, userID:String):  LiveData<Resource<List<User>>>  {
       return roomUserJoinRepository.getListUserSuggested(type,userID)
    }
    override fun setInviteUsersToRoom(roomId: String, userIds: List<String>) {
        _inviteUserToRoom.value = RoomRepository.InviteUsersToRoomObject(roomId, userIds);
    }
}