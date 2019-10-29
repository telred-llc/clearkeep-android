package vmodev.clearkeep.databases

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Single
import vmodev.clearkeep.viewmodelobjects.User

@Dao
abstract class AbstractUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(user: User): Long;

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertUsers(users: List<User>): List<Long>;

    @Query("SELECT * FROM user WHERE id = :id")
    abstract fun findById(id: String): LiveData<User>;

    @Query("SELECT User.* FROM User WHERE User.id =:id")
    abstract fun getUserById(id: String): User;

    @Query("SELECT * FROM User")
    abstract fun findAll(): Single<List<User>>

    @Query("UPDATE user SET name = :name, avatarUrl = :avatarUrl WHERE id =:id")
    abstract fun updateUser(id: String, name: String, avatarUrl: String): Int;

    @Query("UPDATE user SET status =:status WHERE id=:id")
    abstract fun updateStatus(id: String, status: Byte): Int;

    @Query("SELECT * FROM user WHERE name =:name")
    abstract fun findUsers(name: String): LiveData<List<User>>

    @Query("SELECT name FROM user WHERE id =:id")
    abstract fun getUserNameById(id: String): LiveData<String>

    @Query("UPDATE user SET name =:name WHERE id =:id")
    abstract fun updateUserName(id: String, name: String)

    @Query("UPDATE user SET name =:name, avatarUrl =:avatarUrl WHERE id=:id")
    abstract fun updateUserNameAndAvatarUrl(id: String, name: String, avatarUrl: String)

    @Update
    abstract fun updateUsers(users: List<User>): Int;

    @Update
    abstract fun updateUser(item: User): Int;

    @Query("DELETE FROM user")
    abstract fun delete();

    @Query("SELECT user.* FROM user INNER JOIN roomUserJoin ON user.id = roomUserJoin.user_id INNER JOIN room ON room.id = roomUserJoin.room_id WHERE room.id =:roomId")
    abstract fun getUsersWithRoomId(roomId: String): LiveData<List<User>>

    @Query("SELECT user.* FROM user INNER JOIN roomUserJoin ON user.id = roomUserJoin.user_id INNER JOIN room ON room.id = roomUserJoin.room_id WHERE room.id =:roomId")
    abstract fun getUsersWithRoomIdRx(roomId: String): Single<List<User>>

    @Query("SELECT * FROM user WHERE id IN (:ids)")
    abstract fun getUsersWithId(ids: Array<String>): LiveData<List<User>>

    @Query("SELECT * FROM User ORDER BY RANDOM() LIMIT 10")
    abstract fun getListUserSuggested(): LiveData<List<User>>


}