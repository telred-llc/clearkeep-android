package vmodev.clearkeep.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import vmodev.clearkeep.viewmodelobjects.Message

@Dao
abstract class AbstractMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(message: Message)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertListMessage(messages: List<Message>)

    @Query("SELECT * FROM message INNER JOIN room WHERE room.id =:roomId")
    abstract fun getListMessageWithRoomId(roomId: String): LiveData<List<Message>>
}