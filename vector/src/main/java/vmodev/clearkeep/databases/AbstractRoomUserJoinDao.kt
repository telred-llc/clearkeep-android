package vmodev.clearkeep.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.RoomDatabase.MAX_BIND_PARAMETER_CNT
import android.arch.persistence.room.util.StringUtil
import android.database.Cursor
import android.support.v4.util.ArrayMap
import vmodev.clearkeep.viewmodelobjects.*
import vmodev.clearkeep.viewmodelobjects.Room
import java.util.ArrayList

@Dao
abstract class AbstractRoomUserJoinDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(roomUserJoin: RoomUserJoin): Long;

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertRoomUserJoins(roomUserJoins: List<RoomUserJoin>): List<Long>;

    @Query("SELECT user.* FROM user INNER JOIN roomUserJoin ON user.id = roomUserJoin.user_id INNER JOIN room ON room.id = roomUserJoin.room_id WHERE room.id =:roomId")
    abstract fun getUsersWithRoomId(roomId: String): LiveData<List<User>>

    @Query("SELECT room.* FROM room INNER JOIN roomUserJoin ON room.id = roomUserJoin.room_id INNER JOIN user ON user.id = roomUserJoin.user_id WHERE user.id =:userId")
    abstract fun getRoomsWithUserId(userId: String): LiveData<List<Room>>

    @Update
    abstract fun updateRoomUserJoin(items: List<RoomUserJoin>): Int;

    @Query("SELECT room.* FROM room INNER JOIN roomUserJoin ON room.id = roomUserJoin.room_id INNER JOIN user ON user.id = roomUserJoin.user_id WHERE user.id =:userId AND (room.type == 1 OR room.type == 65 OR room.type == 129)")
    abstract fun getDirectChatRoomWithUserId(userId: String): LiveData<List<Room>>;

    @Query("SELECT room.* FROM room INNER JOIN roomUserJoin ON room.id = roomUserJoin.room_id INNER JOIN user ON user.id = roomUserJoin.user_id WHERE roomUserJoin.user_id =:userId AND (room.type == 2 OR room.type == 66 OR room.type == 130)")
    abstract fun getRoomChatRoomWithUserId(userId: String): LiveData<List<Room>>;

    @Query("SELECT roomUserJoin.* FROM roomUserJoin INNER JOIN room ON room.id = roomUserJoin.room_id WHERE room.id =:roomId")
    abstract fun getListRoomUserJoinWithRoomId(roomId: String): LiveData<List<RoomUserJoin>>;

    @Query("DELETE FROM roomUserJoin")
    abstract fun delete();

    @Query("SELECT roomUserJoin.* FROM roomUserJoin INNER JOIN room ON room.id = roomUserJoin.room_id INNER JOIN user ON user.id = roomUserJoin.user_id WHERE room.id =:roomId AND user.id =:userId")
    abstract fun getRoomUserJoinWithRoomIdAndUserId(roomId: String, userId: String): LiveData<RoomUserJoin>;

    @Query("SELECT RoomUserJoin.*, Room.*, User.* FROM RoomUserJoin INNER JOIN Room ON RoomUserJoin.room_id = Room.id INNER JOIN User ON RoomUserJoin.user_id = User.id WHERE room.type =:type")
    abstract fun getListRoomListUserOne(type: Int): LiveData<List<RoomListUser>>

    @Query("SELECT roomUserJoin.*, room.*, user.* FROM RoomUserJoin INNER JOIN Room ON roomUserJoin.room_id = room.id INNER JOIN user ON roomUserJoin.user_id = user.id WHERE room.type =:typeOne OR room.type =:typeTwo")
    abstract fun getListRoomListUserTwo(typeOne: Int, typeTwo: Int): LiveData<List<RoomListUser>>

    @Query("SELECT DISTINCT roomUserJoin.room_id, room.* FROM roomUserJoin INNER JOIN room ON roomUserJoin.room_id = room.id WHERE room.type =:typeOne OR room.type =:typeTwo")
    abstract fun getListRoomUserListTwo(typeOne: Int, typeTwo: Int): List<RoomUserList>

    @Query("SELECT DISTINCT RoomUserJoin.*, User.* FROM RoomUserJoin INNER JOIN User ON RoomUserJoin.user_id = User.id WHERE RoomUserJoin.room_id IN (:ids)")
    abstract fun getListUserInRoom(ids: List<String>): List<ListUserRelationRoom>

    fun getListRoomWithUsers(typeOne: Int, typeTwo: Int): List<ListUserRelationRoom> {
        val list = getListRoomUserListTwo(typeOne, typeTwo);
        val listIds = ArrayList<String>();
        list.forEach {
            it.room?.let {
                listIds.add(it.id)
            }
        }
        return getListUserInRoom(listIds);
    }

    fun getListRoomListUser(filters: Array<Int>): LiveData<List<RoomListUser>> {
        when (filters.size) {
            1 -> return getListRoomListUserOne(filters[0])
            2 -> return getListRoomListUserTwo(filters[0], filters[1])
            else -> return getListRoomListUserOne(0)
        }
    }
}