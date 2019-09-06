package vmodev.clearkeep.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
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

    @Query("SELECT roomUserJoin.* FROM roomUserJoin INNER JOIN room ON room.id = roomUserJoin.room_id INNER JOIN user ON user.id = roomUserJoin.user_id WHERE room.id =:roomId AND user.id =:userId")
    abstract fun getRoomUserJoinWithRoomIdAndUserIdRx(roomId: String, userId: String): Single<RoomUserJoin>;

    @Query("SELECT DISTINCT roomUserJoin.room_id, Room.*, Message.message_id FROM Room LEFT JOIN RoomUserJoin ON roomUserJoin.room_id = Room.id LEFT JOIN Message ON Room.message_id = Message.message_id WHERE room.type =:typeOne ORDER BY room.type DESC, room.updatedDate DESC")
    abstract fun getListRoomListUserWithFilter(typeOne: Int): LiveData<List<RoomListUser>>

    @Query("SELECT DISTINCT roomUserJoin.room_id, Room.*, Message.message_id FROM Room LEFT JOIN RoomUserJoin ON roomUserJoin.room_id = Room.id LEFT JOIN Message ON Room.message_id = Message.message_id WHERE room.type =:typeOne OR room.type =:typeTwo ORDER BY room.type DESC, room.updatedDate DESC")
    abstract fun getListRoomListUserWithFilter(typeOne: Int, typeTwo: Int): LiveData<List<RoomListUser>>

    @Query("SELECT DISTINCT roomUserJoin.room_id, room.* FROM roomUserJoin INNER JOIN room ON roomUserJoin.room_id = room.id WHERE room.type =:typeOne OR room.type =:typeTwo")
    abstract fun getListRoomUserListTwo(typeOne: Int, typeTwo: Int): LiveData<List<RoomUserList>>

    @Query("SELECT DISTINCT User.* FROM User WHERE User.id IN (:ids)")
    abstract fun getListUserInRoom(ids: List<String>): LiveData<List<User>>

    @Query("SELECT DISTINCT RoomUserJoin.room_id, Room.* FROM RoomUserJoin INNER JOIN Room ON RoomUserJoin.room_id = Room.id INNER JOIN User ON RoomUserJoin.user_id = User.id WHERE User.id =:userId AND type =:filterOne")
    abstract fun getListRoomListUserWithFilterAndUserId(userId: String, filterOne: Int): LiveData<List<RoomListUser>>;

    @Query("SELECT DISTINCT RoomUserJoin.room_id, Room.* FROM RoomUserJoin INNER JOIN Room ON RoomUserJoin.room_id = Room.id INNER JOIN User ON RoomUserJoin.user_id = User.id WHERE User.id =:userId AND (Room.type =:filterOne OR Room.type =:filterTwo)")
    abstract fun getListRoomListUserWithFilterAndUserId(userId: String, filterOne: Int, filterTwo: Int): LiveData<List<RoomListUser>>;

    @Query("SELECT DISTINCT RoomUserJoin.room_id, Room.* FROM RoomUserJoin INNER JOIN Room ON RoomUserJoin.room_id = Room.id INNER JOIN User ON RoomUserJoin.user_id = User.id WHERE User.id =:userId AND (Room.type =:filterOne OR Room.type =:filterTwo OR Room.type =:filterThree)")
    abstract fun getListRoomListUserWithFilterAndUserId(userId: String, filterOne: Int, filterTwo: Int, filterThree: Int): LiveData<List<RoomListUser>>;

    @Query("SELECT DISTINCT RoomUserJoin.room_id, Room.* FROM RoomUserJoin INNER JOIN Room ON RoomUserJoin.room_id = Room.id WHERE room.id IN (:roomIds)")
    abstract fun getListRoomListUserWithListRoomId(roomIds: List<String>): LiveData<List<RoomListUser>>;

    @Query("SELECT DISTINCT RoomUserJoin.room_id, Room.* FROM RoomUserJoin INNER JOIN Room ON RoomUserJoin.room_id = Room.id WHERE Room.name LIKE :query AND type IN(:filters)")
    abstract fun findRoomByNameContain(filters: List<Int>, query: String): LiveData<List<RoomListUser>>

    @Query("SELECT DISTINCT roomUserJoin.room_id, Room.*, Message.room_id FROM Room INNER JOIN RoomUserJoin ON roomUserJoin.room_id = Room.id INNER JOIN Message ON Message.message_id = Room.message_id WHERE room.type =:typeOne OR room.type =:typeTwo ORDER BY room.type DESC, room.updatedDate DESC")
    abstract fun getListRoomListUserWithFilterTest(typeOne: Int, typeTwo: Int): List<RoomListUser>

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
            1 -> return getListRoomListUserWithFilter(filters[0])
            2 -> return getListRoomListUserWithFilter(filters[0], filters[1])
            else -> return getListRoomListUserWithFilter(0)
        }
    }

    fun getListRoomWithFilterAndUserId(userId: String, filters: Array<Int>): LiveData<List<RoomListUser>> {
        when (filters.size) {
            1 -> return getListRoomListUserWithFilterAndUserId(userId, filters[0]);
            2 -> return getListRoomListUserWithFilterAndUserId(userId, filters[0], filters[1]);
            3 -> return getListRoomListUserWithFilterAndUserId(userId, filters[0], filters[1], filters[2])
            else -> return getListRoomListUserWithFilterAndUserId(userId, 0);
        }
    }
}