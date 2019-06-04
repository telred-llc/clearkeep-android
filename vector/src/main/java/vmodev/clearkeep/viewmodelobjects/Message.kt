package vmodev.clearkeep.viewmodelobjects

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["messageId"]
//        , foreignKeys = [
//    ForeignKey(entity = Room::class,
//            parentColumns = ["id"],
//            childColumns = ["room_id"],
//            onDelete = ForeignKey.CASCADE),
//    ForeignKey(entity = User::class,
//            parentColumns = ["id"],
//            childColumns = ["user_id"],
//            onDelete = ForeignKey.CASCADE)
//],
//        indices = [
//            Index(value = ["room_id"]),
//            Index(value = ["user_id"])
//        ]
)
data class Message(
        @field:SerializedName("messageId") val messageId: String,
        @field:SerializedName("encrypted_content") val encryptedContent: String,
        @field:SerializedName("message_type") val messageType: String,
        @field:SerializedName("room_id") val roomId: String,
        @field:SerializedName("user_id") val userId: String
//        @ColumnInfo(name = "room_id") val roomId: String,
//        @ColumnInfo(name = "user_id") val userId: String
)