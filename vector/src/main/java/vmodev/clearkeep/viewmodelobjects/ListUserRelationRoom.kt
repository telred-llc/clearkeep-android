package vmodev.clearkeep.viewmodelobjects

import androidx.room.Embedded
import androidx.room.Relation


data class ListUserRelationRoom(
        @Embedded var roomUserJoin: RoomUserJoin? = null
        , @Relation(parentColumn = "user_id", entityColumn = "id", entity = User::class) var users: List<User>? = null
)