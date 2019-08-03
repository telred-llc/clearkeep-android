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
import vmodev.clearkeep.viewmodels.interfaces.AbstractSplashActivityViewModel
import javax.inject.Inject

class SplashActivityViewModel @Inject constructor(private val roomRepository: RoomRepository, private val userRepository: UserRepository, private val roomUserJoinRepository: RoomUserJoinRepository) : AbstractSplashActivityViewModel() {
    override fun getAllRoomResult(filters: Array<Int>): LiveData<Resource<List<Room>>> {
        return roomRepository.updateAndCreateListRoomRx(filters);
    }

    override fun getUpdateUserResult(roomId: String): LiveData<Resource<List<User>>> {
        return userRepository.getListUserInRoomFromNetwork(roomId);
    }

    override fun getUpdateRoomUserJoinResult(roomId: String, userId: String) {
        roomUserJoinRepository.insertRoomUserJoin(roomId, userId);
    }
}