package vmodev.clearkeep.viewmodelobjects

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class RoomUserList(
        @Embedded
        var room: Room? = null
        , @Relation(parentColumn = "id", entityColumn = "room_id", entity = RoomUserJoin::class)
        var roomUserJoin: List<RoomUserJoin>? = null
//        , @Relation(parentColumn = "room_id", entityColumn = "id", entity = User::class)
//        var users: List<User>? = null
)