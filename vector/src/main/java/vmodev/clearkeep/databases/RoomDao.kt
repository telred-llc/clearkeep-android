package vmodev.clearkeep.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import vmodev.clearkeep.viewmodelobjects.Room

@Dao
abstract class RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg room: Room);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRooms(rooms: List<Room>);

    @Query("SELECT * FROM room WHERE type = :type")
    abstract fun loadWithType(type: Int): LiveData<List<Room>>;

    @Query("SELECT * FROM room WHERE id =:id")
    abstract fun findById(id: String): LiveData<Room>;

    @Query("UPDATE room SET name =:name, type =:type, updatedDate =:updatedDate, avatarUrl =:avatarUrl, notifyCount =:notifyCount  WHERE id =:id")
    abstract fun updateRoom(id: String, name: String, type: Int, updatedDate: Long, avatarUrl: String, notifyCount: Int)

    @Query("SELECT * FROM room WHERE type =:filterOne OR type =:filterTwo")
    abstract fun loadWithType(filterOne: Int, filterTwo: Int): LiveData<List<Room>>;
}