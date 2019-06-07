package vmodev.clearkeep.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomUserJoin
import vmodev.clearkeep.viewmodelobjects.User

@Dao
abstract class AbstractRoomUserJoinDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(roomUserJoin: RoomUserJoin): Long;

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertRoomUserJoins(roomUserJoins: List<RoomUserJoin>): List<Long>;

    @Query("SELECT * FROM user INNER JOIN roomUserJoin ON user.id = roomUserJoin.user_id WHERE roomUserJoin.room_id =:roomId")
    abstract fun getUsersWithRoomId(roomId: String): LiveData<List<User>>

    @Query("SELECT * FROM room INNER JOIN roomUserJoin ON room.id = roomUserJoin.room_id WHERE roomUserJoin.user_id =:userId")
    abstract fun getRoomsWithUserId(userId: String): LiveData<List<Room>>

    @Update
    abstract fun updateRoomUserJoin(items: List<RoomUserJoin>): Int;
//
//    @Query("DELETE FROM roomUserJoin WHERE roomUserJoin.room_id =:roomId")
//    abstract fun deleteRoomUserJoinWithRoomId(roomId: String);
}