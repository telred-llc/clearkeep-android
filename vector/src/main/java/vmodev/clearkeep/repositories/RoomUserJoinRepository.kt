package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import vmodev.clearkeep.databases.AbstractRoomUserJoinDao
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomUserJoinRepository @Inject constructor(private val roomUserJoinDao: AbstractRoomUserJoinDao) {
    fun getDirectChatRoomByUserId(userId: String): LiveData<Resource<List<Room>>> {
        return object : AbstractLocalBoundSource<List<Room>>() {
            override fun loadFromDb(): LiveData<List<Room>> {
                return roomUserJoinDao.getDirectChatRoomWithUserId(userId);
            }
        }.asLiveData();
    }

    fun getRoomChatRoomByUserId(userId: String): LiveData<Resource<List<Room>>> {
        return object : AbstractLocalBoundSource<List<Room>>() {
            override fun loadFromDb(): LiveData<List<Room>> {
                return roomUserJoinDao.getRoomChatRoomWithUserId(userId);
            }
        }.asLiveData();
    }
}