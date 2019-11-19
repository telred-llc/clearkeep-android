package vmodev.clearkeep.jsonmodels

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import com.google.gson.annotations.Expose



class CallComingContent : Serializable {
    @SerializedName("room_id")
    @Expose
    private var roomId: String? = null
    @SerializedName("type")
    @Expose
    private var type: String? = null
    @SerializedName("content")
    @Expose
    private var content: Content? = null

    fun getRoomId(): String? {
        return roomId
    }
    fun setRoomId(roomId: String) {
        this.roomId = roomId
    }
    fun getType(): String? {
        return type
    }

    fun getContent(): Content? {
        return content
    }

    public class Content{
        @SerializedName("call_id")
        @Expose
        private val callId: String? = null
    }

}
