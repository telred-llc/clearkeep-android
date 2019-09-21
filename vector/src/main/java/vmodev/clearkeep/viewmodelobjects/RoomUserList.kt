package vmodev.clearkeep.viewmodelobjects

import androidx.lifecycle.LiveData
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation

data class RoomUserList(
        @Embedded
        var room: Room? = null
        , @Relation(parentColumn = "id", entityColumn = "room_id", entity = RoomUserJoin::class)
        var roomUserJoin: List<RoomUserJoin>? = null
        , @Ignore
        var users: LiveData<List<User>>? = null
)