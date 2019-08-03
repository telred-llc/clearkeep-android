package vmodev.clearkeep.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

@Dao
abstract class AbstractRoomDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(room: Room): Long;

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertRooms(rooms: List<Room>): List<Long>;

    @Query("SELECT * FROM room WHERE type = :type ORDER BY updatedDate DESC")
    abstract fun loadWithType(type: Int): LiveData<List<Room>>;

    @Query("SELECT * FROM room WHERE id =:id")
    abstract fun findById(id: String): LiveData<Room>;

    @Query("SELECT name FROM room WHERE id =:id")
    abstract fun findNameById(id: String): LiveData<String>;

    @Query("UPDATE room SET name =:name, type =:type, updatedDate =:updatedDate, avatarUrl =:avatarUrl, notifyCount =:notifyCount  WHERE id =:id")
    abstract fun updateRoom(id: String, name: String, type: Int, updatedDate: Long, avatarUrl: String, notifyCount: Int)

    @Query("UPDATE room SET type =:type WHERE id =:id")
    abstract fun updateRoom(id: String, type: Int);

    @Query("SELECT * FROM room WHERE type =:filterOne OR type =:filterTwo ORDER BY type DESC, updatedDate DESC")
    abstract fun loadWithType(filterOne: Int, filterTwo: Int): LiveData<List<Room>>;

    @Query("SELECT * FROM room WHERE type =:filterOne OR type =:filterTwo ORDER BY updatedDate DESC")
    abstract fun loadWithTypeOnlyTime(filterOne: Int, filterTwo: Int): LiveData<List<Room>>;

    @Query("SELECT * FROM room WHERE type =:filterOne OR type =:filterTwo OR type =:filterThree ORDER BY type DESC, updatedDate DESC")
    abstract fun loadWithType(filterOne: Int, filterTwo: Int, filterThree: Int): LiveData<List<Room>>;

    @Query("SELECT * FROM room WHERE type =:filterOne OR type =:filterTwo OR type =:filterThree OR type =:filterFour ORDER BY type DESC, updatedDate DESC")
    abstract fun loadWithType(filterOne: Int, filterTwo: Int, filterThree: Int, filterFour: Int): LiveData<List<Room>>;

    @Query("SELECT * FROM room WHERE type =:filterOne OR type =:filterTwo OR type =:filterThree OR type =:filterFour OR type =:filterFive ORDER BY type DESC, updatedDate DESC")
    abstract fun loadWithType(filterOne: Int, filterTwo: Int, filterThree: Int, filterFour: Int, filterFive: Int): LiveData<List<Room>>;

    @Query("SELECT * FROM room WHERE type =:filterOne OR type =:filterTwo OR type =:filterThree OR type =:filterFour OR type =:filterFive OR type =:filterSix ORDER BY type DESC, updatedDate DESC")
    abstract fun loadWithType(filterOne: Int, filterTwo: Int, filterThree: Int, filterFour: Int, filterFive: Int, filterSix: Int): LiveData<List<Room>>;

    @Query("DELETE FROM room WHERE id =:id")
    abstract fun deleteRoom(id: String);

    @Query("UPDATE room SET type =:type WHERE id =:id")
    abstract fun updateType(id: String, type: Int)

    @Query("SELECT * FROM room WHERE id IN (:ids)")
    abstract fun getListRoomWithListId(ids: List<String>): LiveData<List<Room>>;

    @Update
    abstract fun updateRooms(rooms: List<Room>): Int;

    @Update
    abstract fun updateRoom(room: Room): Int;

    @Query("UPDATE room SET notifyCount =:count WHERE room.id =:id")
    abstract fun updateNotifyCount(id: String, count: Int);

    @Query("SELECT * FROM room WHERE id =:roomId")
    abstract fun findByIdSync(roomId: String): Room?;

    @Query("SELECT * FROM room WHERE (type =:filterOne OR type =:filterTwo) AND name LIKE :query")
    abstract fun searchWithDisplayName(filterOne: Int, filterTwo: Int, query: String): LiveData<List<Room>>

    @Query("UPDATE room SET notificationState =:state WHERE room.id =:id")
    abstract fun updateNotificationState(id: String, state: Byte): Int;

    @Query("DELETE FROM room")
    abstract fun delete();

    @Query("SELECT room.* FROM room INNER JOIN roomUserJoin ON room.id = roomUserJoin.room_id INNER JOIN user ON user.id = roomUserJoin.user_id WHERE user.id =:userId AND (room.type == 1 OR room.type == 65 OR room.type == 129)")
    abstract fun getDirectChatRoomWithUserId(userId: String): LiveData<List<Room>>;

    @Query("SELECT room.* FROM room INNER JOIN roomUserJoin ON room.id = roomUserJoin.room_id INNER JOIN user ON user.id = roomUserJoin.user_id WHERE roomUserJoin.user_id =:userId AND (room.type == 2 OR room.type == 66 OR room.type == 130)")
    abstract fun getRoomChatRoomWithUserId(userId: String): LiveData<List<Room>>;

    fun loadWithType(filter: Array<Int>): LiveData<List<Room>> {
        when (filter.size) {
            1 -> return loadWithType(filter[0]);
            2 -> return loadWithType(filter[0], filter[1])
            3 -> return loadWithType(filter[0], filter[1], filter[2])
            4 -> return loadWithType(filter[0], filter[1], filter[2], filter[3])
            5 -> return loadWithType(filter[0], filter[1], filter[2], filter[3], filter[4])
            6 -> return loadWithType(filter[0], filter[1], filter[2], filter[3], filter[4], filter[5])
            else -> return loadWithType(0);
        }
    }

    fun loadWithTypeOnlyTime(filter: Array<Int>): LiveData<List<Room>> {
        when (filter.size) {
            1 -> return loadWithType(filter[0]);
            2 -> return loadWithTypeOnlyTime(filter[0], filter[1])
            3 -> return loadWithType(filter[0], filter[1], filter[2])
            else -> return loadWithType(0);
        }
    }
}