package vmodev.clearkeep.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
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

    @Query("SELECT DISTINCT roomUserJoin.room_id, room.* FROM RoomUserJoin INNER JOIN Room ON roomUserJoin.room_id = room.id WHERE room.type =:typeOne")
    abstract fun getListRoomListUserOne(typeOne: Int): LiveData<List<RoomListUser>>

    @Query("SELECT DISTINCT roomUserJoin.room_id, room.* FROM RoomUserJoin INNER JOIN Room ON roomUserJoin.room_id = room.id WHERE room.type =:typeOne OR room.type =:typeTwo")
    abstract fun getListRoomListUserTwo(typeOne: Int, typeTwo: Int): LiveData<List<RoomListUser>>

    @Query("SELECT DISTINCT roomUserJoin.room_id, room.* FROM roomUserJoin INNER JOIN room ON roomUserJoin.room_id = room.id WHERE room.type =:typeOne OR room.type =:typeTwo")
    abstract fun getListRoomUserListTwo(typeOne: Int, typeTwo: Int): LiveData<List<RoomUserList>>

    @Query("SELECT DISTINCT User.* FROM User WHERE User.id IN (:ids)")
    abstract fun getListUserInRoom(ids: List<String>): LiveData<List<User>>

    fun getListRoomWithUsers(typeOne: Int, typeTwo: Int): LiveData<List<RoomUserList>> {
        val list = getListRoomUserListTwo(typeOne, typeTwo);

        list.value?.forEach {
            val usersIdTmp = mutableListOf<String>();
            it.roomUserJoin?.let {
                it.forEach { usersIdTmp.add(it.userId) }
            }
            it.users = getListUserInRoom(usersIdTmp);
        }
        return list;
    }

    fun getListRoomListUser(filters: Array<Int>): LiveData<List<RoomListUser>> {
        when (filters.size) {
            1 -> return getListRoomListUserOne(filters[0])
            2 -> return getListRoomListUserTwo(filters[0], filters[1])
            else -> return getListRoomListUserOne(0)
        }
    }
}