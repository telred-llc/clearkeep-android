package vmodev.clearkeep.viewmodelobjects

import android.arch.persistence.room.*
import com.google.gson.annotations.SerializedName
import im.vector.adapters.ImageCompressionDescription

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