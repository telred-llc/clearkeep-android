package vmodev.clearkeep.viewmodelobjects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"],
        foreignKeys = [
            ForeignKey(entity = KeyBackup::class
                    , parentColumns = ["id"]
                    , childColumns = ["key_backup_id"]
                    , onDelete = ForeignKey.CASCADE)
        ], indices = [Index(value = ["key_backup_id"])])
data class Signature(
        @ColumnInfo(name = "id") @field:SerializedName("id") val id: String,
        @ColumnInfo(name = "status") @field:SerializedName("status") val status: Byte,
        @ColumnInfo(name = "description") @field:SerializedName("description") val description: String,
        @ColumnInfo(name = "key_backup_id") val keyBackup: String
)