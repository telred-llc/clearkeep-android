package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.repositories.MessageRepository
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.RoomUserJoinRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractSplashActivityViewModel
import javax.inject.Inject

class SplashActivityViewModel @Inject constructor(private val roomRepository: RoomRepository
                                                  , private val userRepository: UserRepository
                                                  , private val roomUserJoinRepository: RoomUserJoinRepository
                                                  , private val messageRepository: MessageRepository) : AbstractSplashActivityViewModel() {
    override fun getAllRoomResult(filters: Array<Int>): LiveData<Resource<List<Room>>> {
        return roomRepository.updateAndCreateListRoomRx(filters)
    }

    override fun getUpdateUserResult(roomId: String): LiveData<Resource<List<User>>> {
        return userRepository.getListUserInRoomFromNetwork(roomId)
    }

    override fun getUpdateRoomUserJoinResult(roomId: String, userId: String) {
        roomUserJoinRepository.insertRoomUserJoin(roomId, userId)
    }

    override fun getUpdateLastMessageResult(roomId: String): Observable<Message> {
        return messageRepository.getLastMessageOfRoom(roomId)
    }

    override fun updateRoomLastMessage(roomId: String, messageId: String) {
        roomRepository.updateLastMessage(roomId, messageId).subscribeOn(Schedulers.io()).subscribe()
    }

    override fun updateUsersFromRoom(roomId: String): Observable<List<User>> {
        return userRepository.getListUserInRoomFromNetworkRx(roomId)
    }

    override fun getAllRoomResultRx(filters: Array<Int>): Observable<List<Room>> {
        return roomRepository.updateAndCreateListRoomReturnRx(filters)
    }

    override fun updateRoomUserCreated(roomId: String, userId: String) {
        return roomRepository.updateUserCreated(roomId, userId)
    }
}