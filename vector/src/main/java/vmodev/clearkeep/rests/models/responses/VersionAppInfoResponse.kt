package vmodev.clearkeep.rests.models.responses

import android.util.Log
import com.google.gson.annotations.SerializedName

data class VersionAppInfoResponse(

        @SerializedName("createdAt")
        val createdAt: Int? = null,

        @SerializedName("id")
        val id: Int? = null,

        @SerializedName("type")
        val type: String? = null,

        @SerializedName("version")
        val version: String? = null
) {
    fun trace() {
        Log.e("Tag", "--- id: ${id}\ntype: $type\nversion: $version\ncreatedAt: $createdAt")
    }
}