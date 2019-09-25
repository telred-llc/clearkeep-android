package vmodev.clearkeep.viewmodelobjects

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["id"])
data class KeyBackup(
        @ColumnInfo(name = "id") val id: String,
        @ColumnInfo(name = "version") val version: String,
        @ColumnInfo(name = "algorithm") val algorithm: String,
        @ColumnInfo(name = "count") val count: Int,
        @ColumnInfo(name = "state") val state : Int
)