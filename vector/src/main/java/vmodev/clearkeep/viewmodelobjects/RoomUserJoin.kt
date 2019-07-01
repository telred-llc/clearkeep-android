package vmodev.clearkeep.viewmodelobjects

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.Index

@Entity(primaryKeys = ["room_id", "user_id"],
        foreignKeys = [
            ForeignKey(entity = Room::class,
                    parentColumns = ["id"],
                    childColumns = ["room_id"],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(entity = User::class,
                    parentColumns = ["id"],
                    childColumns = ["user_id"],
                    onDelete = ForeignKey.CASCADE)
        ],
        indices = [
            Index(value = ["room_id"]),
            Index(value = ["user_id"])
        ])
data class RoomUserJoin(
        @ColumnInfo(name = "room_id") val roomId: String,
        @ColumnInfo(name = "user_id") val userId: String
)