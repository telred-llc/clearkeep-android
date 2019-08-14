package vmodev.clearkeep.viewmodelobjects

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class ListUserRelationRoom(
        @Embedded var roomUserJoin: RoomUserJoin? = null
        , @Relation(parentColumn = "user_id", entityColumn = "id", entity = User::class) var users: List<User>? = null
)