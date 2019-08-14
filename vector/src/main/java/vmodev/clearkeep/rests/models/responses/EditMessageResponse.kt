package vmodev.clearkeep.rests.models.responses

import com.google.gson.annotations.SerializedName

data class EditMessageResponse(@SerializedName("event_id") val eventId: String)