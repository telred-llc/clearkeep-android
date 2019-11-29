package vmodev.clearkeep.viewmodelobjects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.google.gson.annotations.SerializedName

@Entity(
        primaryKeys = ["message_id"],

        foreignKeys = [
            ForeignKey(entity = Room::class,
                    parentColumns = ["id"],
                    childColumns = ["room_id"],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(entity = User::class,
                    parentColumns = ["id"],
                    childColumns = ["user_id"],
                    onDelete = ForeignKey.CASCADE)
        ], indices = [Index(value = ["room_id"]),
    Index(value = ["user_id"])]
)
data class Message(
        @ColumnInfo(name = "message_id") @field:SerializedName("id") val id: String,
        @field:SerializedName("encrypted_content") val encryptedContent: String,
        @field:SerializedName("message_type") val messageType: String,
        @ColumnInfo(name = "room_id") val roomId: String,
        @ColumnInfo(name = "user_id") val userId: String,
        @ColumnInfo(name = "user_key") val userKey : String?,
        @ColumnInfo(name = "created_at") val createdAt : Long
)