package vmodev.clearkeep.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import vmodev.clearkeep.viewmodelobjects.Room

@Dao
abstract class RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(room: Room);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRooms(rooms: List<Room>);

    @Query("SELECT * FROM room WHERE type = :type")
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

    @Query("SELECT * FROM room WHERE type =:filterOne OR type =:filterTwo OR type =:filterThree ORDER BY type DESC, updatedDate DESC")
    abstract fun loadWithType(filterOne: Int, filterTwo: Int, filterThree: Int): LiveData<List<Room>>;

    @Query("DELETE FROM room WHERE id =:id")
    abstract fun deleteRoom(id: String);

    @Query("UPDATE room SET roomMemberStatus =:roomMemberStatus WHERE roomMemberId =:roomMemberId")
    abstract fun updateRoomMemberStatus(roomMemberId: String, roomMemberStatus: Byte);

    fun loadWithType(filter: Array<Int>): LiveData<List<Room>> {
        when (filter.size) {
            1 -> return loadWithType(filter[0]);
            2 -> return loadWithType(filter[0], filter[1])
            3 -> return loadWithType(filter[0], filter[1], filter[2])
            else -> return loadWithType(0);
        }
    }
}