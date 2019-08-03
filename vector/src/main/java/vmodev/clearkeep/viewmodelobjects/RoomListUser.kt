package vmodev.clearkeep.viewmodelobjects

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class RoomListUser(
        @Embedded
        var roomUserJoin: RoomUserJoin? = null,
        @Relation(parentColumn = "room_id", entityColumn = "id", entity = Room::class)
        var room: List<Room>? = null,
        @Relation(parentColumn = "user_id", entityColumn = "id", entity = User::class)
        var users: List<User>? = null
)