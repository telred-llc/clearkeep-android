package vmodev.clearkeep.jsonmodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CallContent {
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

    fun getType(): String? {
        return type
    }

    fun getContent(): Content? {
        return content
    }


    class Content {
        @SerializedName("call_id")
        @Expose
        private var callId: String? = null
        @SerializedName("offer")
        @Expose
        private var offer: Offer? = null
        @SerializedName("reason")
        @Expose
        private var reason: String? = null

        fun getOffer(): Offer? {
            return offer
        }

        fun getCallId(): String? {
            return callId
        }

        fun getReason(): String? {
            return reason
        }


        public class Offer {
            @SerializedName("type")
            @Expose
            private var type: String? = null

            fun getType(): String? {
                return type
            }

            fun setType(type: String) {
                this.type = type
            }
        }
    }

}