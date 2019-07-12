package vmodev.clearkeep.viewmodelobjects

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey

@Entity(primaryKeys = ["id"])
data class KeyBackup(
        @ColumnInfo(name = "id") val id: String,
        @ColumnInfo(name = "version") val version: String,
        @ColumnInfo(name = "algorithm") val algorithm: String,
        @ColumnInfo(name = "count") val count: Int
)