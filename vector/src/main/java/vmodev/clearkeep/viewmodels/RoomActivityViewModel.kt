package vmodev.clearkeep.viewmodels

import io.reactivex.Completable
import vmodev.clearkeep.repositories.KeyBackupRepository
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomActivityViewModel
import javax.inject.Inject

class RoomActivityViewModel @Inject constructor(private val userRepository: UserRepository, private val roomRepository: RoomRepository, private val keyBackupRepository: KeyBackupRepository) : AbstractRoomActivityViewModel() {
    override fun setIdForUpdateRoomNotifyCount(roomId: String): Completable {
        return roomRepository.clearnRoomNotificationCount(roomId)
    }

}