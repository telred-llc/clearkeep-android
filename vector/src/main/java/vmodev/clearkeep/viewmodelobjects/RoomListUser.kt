package vmodev.clearkeep.viewmodelobjects

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class RoomListUser(
        @Embedded
        var room: Room? = null,
        @Relation(parentColumn = "id", entityColumn = "id", entity = User::class, associateBy = Junction(
                value = RoomUserJoin::class, entityColumn = "user_id", parentColumn = "room_id"
        ))
        var roomUserJoin: List<User>? = null,
        @Embedded
        var lastMessage: Message? = null,
        @Embedded(prefix = "user__")
        var lastUserMessage: User? = null
)