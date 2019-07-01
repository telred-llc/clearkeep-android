package vmodev.clearkeep.viewmodelobjects

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(
        @field:SerializedName("name") val name: String,
        @PrimaryKey @ColumnInfo(name = "id") @field:SerializedName("id") val id: String,
        @field:SerializedName("avatarUrl") val avatarUrl: String,
        @field:SerializedName("status") val status: Byte,
        @field:SerializedName("roomId") val roomId: String)