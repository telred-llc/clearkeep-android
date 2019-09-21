package vmodev.clearkeep.viewmodelobjects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class File(
        @field:SerializedName("username") val username: String,
        @field:SerializedName("filename") val filename: String,
        @field:SerializedName("date") val date: String,
        @PrimaryKey @ColumnInfo(name = "id") @field:SerializedName("id") val id: String,
        @field:SerializedName("imgUrl") val imgrUrl: String,
        @field:SerializedName("roomId") val roomId: String)