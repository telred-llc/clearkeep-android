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
import vmodev.clearkeep.viewmodels.interfaces.AbstractContactFragmentViewModel
import javax.inject.Inject

class ContactFragmentViewModel @Inject constructor(roomRepository: RoomRepository, private val roomUserJoinRepository: RoomUserJoinRepository, private val userRepository: UserRepository) : AbstractContactFragmentViewModel() {
    private val _listType = MutableLiveData<Array<Int>>();
    private val listRoomByType = Transformations.switchMap(_listType) { input -> roomUserJoinRepository.getRoomListUserSortWithName(input) }
    private val _roomIdForUpdateNotify = MutableLiveData<String>();
    private val _updateRoomNotifyResult = Transformations.switchMap(_roomIdForUpdateNotify) { input -> roomRepository.setRoomNotify(input) }
    override fun getListRoomByType(): LiveData<Resource<List<RoomListUser>>> {
        return listRoomByType;
    }

    override fun setListType(types: Array<Int>) {
        _listType.value = types;
    }

    override fun setIdForUpdateRoomNotify(roomId: String) {
        _roomIdForUpdateNotify.value = roomId;
    }

    override fun getUpdateRoomNotifyResult(): LiveData<Resource<Room>> {
        return _updateRoomNotifyResult;
    }

    override fun getRoomUserJoinResult(userIds : Array<String>): LiveData<Resource<List<User>>> {
        return userRepository.getUsersWithId(userIds);
    }
}