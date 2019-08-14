package vmodev.clearkeep.viewmodelobjects

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

@Entity(primaryKeys = ["id"])
data class LocalSettings(
        @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "theme") val theme: Int
)