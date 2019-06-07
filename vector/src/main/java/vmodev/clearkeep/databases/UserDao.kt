package vmodev.clearkeep.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import vmodev.clearkeep.viewmodelobjects.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User): Long;

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUsers(users: List<User>): List<Long>;

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

    @Query("UPDATE user SET name =:name WHERE id =:id")
    fun updateUserName(id: String, name: String)

    @Query("UPDATE user SET name =:name, avatarUrl =:avatarUrl WHERE id=:id")
    fun updateUserNameAndAvatarUrl(id: String, name: String, avatarUrl: String)

    @Update
    fun updateUsers(users: List<User>): Int;
}