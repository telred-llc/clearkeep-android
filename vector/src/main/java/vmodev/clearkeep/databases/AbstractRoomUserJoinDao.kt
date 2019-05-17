package vmodev.clearkeep.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomUserJoin
import vmodev.clearkeep.viewmodelobjects.User

@Dao
abstract class AbstractRoomUserJoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(roomUserJoin: RoomUserJoin)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRoomUserJoins(roomUserJoins: List<RoomUserJoin>)

    @Query("SELECT * FROM user INNER JOIN roomUserJoin ON user.id = roomUserJoin.user_id WHERE roomUserJoin.room_id =:roomId")
    abstract fun getUsersWithRoomId(roomId: String): LiveData<List<User>>

    @Query("SELECT * FROM room INNER JOIN roomUserJoin ON room.id = roomUserJoin.room_id WHERE roomUserJoin.user_id =:userId")
    abstract fun getRoomsWithUserId(userId: String): LiveData<List<Room>>
}