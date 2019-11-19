package vmodev.clearkeep.databases

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

@Dao
abstract class AbstractMessageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(message: Message): Long;

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertListMessage(messages: List<Message>): List<Long>;

    @Query("SELECT message.* FROM message INNER JOIN room ON message.room_id = room.id WHERE room.id =:roomId")
    abstract fun getListMessageWithRoomId(roomId: String): LiveData<List<Message>>

    @Query("SELECT * FROM message WHERE message_id =:id")
    abstract fun findById(id: String): LiveData<Message>;

    @Query("SELECT * FROM message")
    abstract fun getAllMessage(): LiveData<List<Message>>;

    @Query("SELECT user.* FROM user INNER JOIN message ON message.user_id = user.id INNER JOIN room ON message.room_id = room.id WHERE room.id =:roomId")
    abstract fun getUsersInRoom(roomId: String): LiveData<List<User>>;

    @Query("SELECT user.* FROM user INNER JOIN message ON message.user_id = user.id WHERE message.message_id =:messageId")
    abstract fun getUserByMessageId(messageId: String): LiveData<User>;

    @Query("SELECT room.* FROM room INNER JOIN message ON message.room_id = room.id WHERE room.id =:roomId")
    abstract fun getRoomByRoomId(roomId: String): LiveData<Room>;

    @Query("DELETE FROM message")
    abstract fun delete();

    @Query("SELECT message.*FROM message")
    abstract fun getAllMessageWithRoomAndUser(): LiveData<List<MessageRoomUser>>

    @Query("SELECT Message.* FROM Message WHERE Message.room_id =:roomId")
    abstract fun getMessageWithRoomId(roomId: String): List<Message>;

    @Query("SELECT Message.* FROM Message")
    abstract fun getAllMessageSync(): List<Message>;

    @Query("SELECT Message.* FROM Message WHERE message_id =:id")
    abstract fun findByIdRx(id: String): Single<Message>;

    @Query("SELECT message.*FROM message")
    abstract fun getListCallHistory(): LiveData<List<MessageRoomUser>>


}