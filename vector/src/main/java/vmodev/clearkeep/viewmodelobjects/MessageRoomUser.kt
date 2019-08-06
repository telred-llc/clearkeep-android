package vmodev.clearkeep.viewmodelobjects

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import vmodev.clearkeep.viewmodelobjects.Message
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

data class MessageRoomUser(
        @Embedded var message: Message? = null
        , @Relation(parentColumn = "room_id", entityColumn = "id", entity = Room::class) var room: List<Room>? = null
        , @Relation(parentColumn = "user_id", entityColumn = "id", entity = User::class) var user: List<User>? = null
)
