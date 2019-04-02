package vmodev.clearkeep.viewmodelobjects

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class User(@field:SerializedName("name") val name: String,
                @field:SerializedName("id") val id: String) {
}