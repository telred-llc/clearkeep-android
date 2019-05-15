package vmodev.clearkeep.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import vmodev.clearkeep.viewmodelobjects.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<User>);

    @Query("SELECT * FROM user WHERE id = :id")
    fun findById(id: String): LiveData<User>;

    @Query("UPDATE user SET name = :name, avatarUrl = :avatarUrl WHERE id =:id")
    fun updateUser(id: String, name: String, avatarUrl: String): Int;

    @Query("UPDATE user SET status =:status WHERE id=:id")
    fun updateStatus(id: String, status: Byte)

    @Query("SELECT * FROM user WHERE name =:name")
    fun findUsers(name: String): LiveData<List<User>>

    @Query("SELECT name FROM user WHERE id =:id")
    fun getUserNameById(id: String): LiveData<String>

    @Query("SELECT * FROM user WHERE roomId =:roomId")
    fun getUsersByRoomId(roomId: String): LiveData<List<User>>
}