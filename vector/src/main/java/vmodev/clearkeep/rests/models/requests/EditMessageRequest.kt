package vmodev.clearkeep.rests.models.requests

import com.google.gson.annotations.SerializedName

data class EditMessageRequest(
        @SerializedName("session_id") val sessionId: String,
        val ciphertext: String,
        @SerializedName("device_id") val deviceId: String,
        val algorithm: String,
        @SerializedName("sender_key") val senderId: String,
        @SerializedName("m.relates_to") val relatesTo: Map<String, String>
)