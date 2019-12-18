package vmodev.clearkeep.jsonmodels

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MessageContent : Serializable {

    @SerializedName("content")
    var content: Content? = null
    @SerializedName("room_id")
    var roomId: String? = null
    @SerializedName("type")
    var type: String? = null

    class Content : Serializable {
        @SerializedName("body")
        var body: String? = null
        @SerializedName("msgtype")
        var msgType: String? = null
    }

}