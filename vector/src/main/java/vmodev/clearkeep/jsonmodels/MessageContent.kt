package vmodev.clearkeep.jsonmodels

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MessageContent : Serializable {
    private lateinit var content: Content;
    @SerializedName("room_id")
    private lateinit var roomId: String;
    private lateinit var type: String;

    public fun getContent(): Content {
        return content;
    }

    public fun getRoomId(): String {
        return roomId;
    }

    public fun getType(): String {
        return type;
    }

    class Content : Serializable {
        private lateinit var body: String;
        @SerializedName("msgtype")
        private lateinit var msgType: String;

        public fun getBody(): String {
            return body;
        }

        public fun getMsgType(): String {
            return msgType;
        }
    }
}