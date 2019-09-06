package vmodev.clearkeep.viewmodelobjects

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Entity for store Room Data
 * @property id Int
 * @property name String
 * @property type Int
 * @property updatedDate Long
 * @property avatarUrl String
 * @constructor
 */
@Entity(
        foreignKeys = [
            ForeignKey(entity = Message::class
                    , parentColumns = ["message_id"]
                    , childColumns = ["message_id"]
                    , onDelete = ForeignKey.CASCADE)
        ]
)
data class Room(
        @PrimaryKey @ColumnInfo(name = "id") @field:SerializedName("id") val id: String,
        @field:SerializedName("name") val name: String,
        @field:SerializedName("topic") val topic: String,
        @field:SerializedName("type") val type: Int,
        @field:SerializedName("updatedDate") val updatedDate: Long,
        @field:SerializedName("avatarUrl") val avatarUrl: String,
        @field:SerializedName("notifyCount") val notifyCount: Int,
        @field:SerializedName("version") val version: Int,
        @field:SerializedName("highlightCount") val highlightCount: Int,
        @ColumnInfo(name = "message_id") @field:SerializedName("message_id") val messageId: String?,
        @field:SerializedName("encrypted") val encrypted: Byte,
        @field:SerializedName("status") val status: Byte,
        @field:SerializedName("notification_state") val notificationState: Byte
)