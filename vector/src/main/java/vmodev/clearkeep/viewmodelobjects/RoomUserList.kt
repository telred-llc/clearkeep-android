package vmodev.clearkeep.viewmodelobjects

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.Relation

data class RoomUserList(
        @Embedded
        var room: Room? = null
        , @Relation(parentColumn = "id", entityColumn = "room_id", entity = RoomUserJoin::class)
        var roomUserJoin: List<RoomUserJoin>? = null
        , @Ignore
        var users: LiveData<List<User>>? = null
)