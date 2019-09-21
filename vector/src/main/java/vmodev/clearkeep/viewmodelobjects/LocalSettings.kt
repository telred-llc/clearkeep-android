package vmodev.clearkeep.viewmodelobjects

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(primaryKeys = ["id"])
data class LocalSettings(
        @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "theme") val theme: Int
)