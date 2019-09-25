package vmodev.clearkeep.viewmodelobjects

import androidx.room.Embedded
import androidx.room.Relation

class RoomListUser(
        @Embedded
        var room: Room? = null,
        @Relation(parentColumn = "id", entityColumn = "room_id", entity = RoomUserJoin::class)
        var roomUserJoin: List<RoomUserJoin>? = null,
        @Embedded
        var lastMessage: Message? = null
)