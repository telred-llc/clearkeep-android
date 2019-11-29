package vmodev.clearkeep.rests.models.responses

import com.google.gson.annotations.SerializedName

data class FeedbackResponse(
        @SerializedName("message") val message: String?
)