package vmodev.clearkeep.viewmodelobjects

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
data class BackupKeyPath(
        @field:SerializedName("id") val id: String,
        @field:SerializedName("path") val path: String
)