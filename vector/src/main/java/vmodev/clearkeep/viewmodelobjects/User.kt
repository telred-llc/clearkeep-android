package vmodev.clearkeep.viewmodelobjects

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(@field:SerializedName("name") val name: String,
                @PrimaryKey @field:SerializedName("id") val id: String) {
}