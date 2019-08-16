package vmodev.clearkeep.rests.models.responses

import com.google.gson.annotations.SerializedName

data class EventResponse(
        @SerializedName("origin_server_ts")
        val originServerTs: Long,
        val age: Long,
        val unsigned: Unsigned,
        val type: String,
        @SerializedName("event_id")
        val eventId: String,
        val sender: String,
        @SerializedName("user_id")
        val userId: String,
        @SerializedName("room_id")
        val roomId: String,
        val content: Content

)

data class Unsigned(
        val age: Long
)

data class Content(
        @SerializedName("sender_key")
        val senderKey: String,
        val algorithm: String,
        @SerializedName("session_id")
        val sessionId: String,
        @SerializedName("device_id")
        val deviceId: String,
        val ciphertext: String
)