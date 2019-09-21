package vmodev.clearkeep.viewmodelobjects

import androidx.room.Embedded
import androidx.room.Relation

data class MessageRoomUser(
        @Embedded var message: Message? = null
        , @Relation(parentColumn = "room_id", entityColumn = "id", entity = Room::class) var room: List<Room>? = null
        , @Relation(parentColumn = "user_id", entityColumn = "id", entity = User::class) var user: List<User>? = null
)
