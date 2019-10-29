package vmodev.clearkeep.rests.models.responses

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
        @SerializedName("displayname") val displayName: String?
        , @SerializedName("avatar_url") val avatarUrl: String?
)