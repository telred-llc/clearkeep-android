package vmodev.clearkeep.viewmodelobjects

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
data class Room(
        @field:SerializedName("roomId") val id: Int,
        @field:SerializedName("name") val name: String,
        @field:SerializedName("type") val type: Int
)

enum class RoomType(val value: Int) {
    DIRECT_MESSAGE(0b0000001),
    ROOM(0b000010),
    FAVOURITE(0b000100)
}