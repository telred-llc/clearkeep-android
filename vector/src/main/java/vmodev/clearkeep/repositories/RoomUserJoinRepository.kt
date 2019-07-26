package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import io.reactivex.Observable
import vmodev.clearkeep.databases.AbstractRoomUserJoinDao
import vmodev.clearkeep.databases.AbstractRoomDao
import vmodev.clearkeep.databases.AbstractUserDao
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.repositories.wayloads.AbstractLocalBoundSource
import vmodev.clearkeep.repositories.wayloads.AbstractNetworkBoundSourceRx
import vmodev.clearkeep.ultis.RoomAndRoomUserJoin
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomUserJoinRepository @Inject constructor(private val roomUserJoinDao: AbstractRoomUserJoinDao, private val abstractUserDao: AbstractUserDao, private val abstractRoomDao: AbstractRoomDao, private val matrixService: MatrixService) {
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

    fun getListRoomUserJoinWithRoomId(roomId: String): LiveData<Resource<List<User>>> {
        return object : AbstractLocalBoundSource<List<User>>() {
            override fun loadFromDb(): LiveData<List<User>> {
                return roomUserJoinDao.getUsersWithRoomId(roomId);
            }
        }.asLiveData();
    }

     fun getUsersInRoom(roomId: String) : LiveData<Resource<List<User>>>{
         return object : AbstractNetworkBoundSourceRx<List<User>, RoomAndRoomUserJoin>(){
             override fun saveCallResult(item: RoomAndRoomUserJoin) {
                 abstractUserDao.insertUsers(item.users);
                 abstractRoomDao.insert(item.room);
                 roomUserJoinDao.insertRoomUserJoins(item.roomUserJoins);
             }

             override fun shouldFetch(data: List<User>?): Boolean {
                 return true;
             }

             override fun loadFromDb(): LiveData<List<User>> {
                 return roomUserJoinDao.getUsersWithRoomId(roomId);
             }

             override fun createCall(): Observable<RoomAndRoomUserJoin> {
                 return matrixService.getUsersInRoomAndAddToRoomUserJoin(roomId);
             }
         }.asLiveData();
     }
}