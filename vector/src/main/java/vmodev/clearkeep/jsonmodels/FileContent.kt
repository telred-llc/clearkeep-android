package vmodev.clearkeep.jsonmodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class FileContent {
    @SerializedName("content")
    @Expose
    private var content: Content? = null
    @SerializedName("room_id")
    @Expose
    private var roomId: String? = null
    @SerializedName("type")
    @Expose
    private var type: String? = null

    fun getContent(): Content? {
        return content
    }


    fun getRoomId(): String? {
        return roomId
    }

    fun getType(): String? {
        return type
    }

    class Content {
        @SerializedName("body")
        @Expose
        private var body: String? = null
        @SerializedName("file")
        @Expose
        private var file: File? = null
        @SerializedName("info")
        @Expose
        private var info: Info? = null
        @SerializedName("msgtype")
        @Expose
        private var msgtype: String? = null

        fun getBody(): String? {
            return body
        }

        fun getFile(): File? {
            return file
        }

        fun getInfo(): Info? {
            return info
        }

        fun getMsgtype(): String? {
            return msgtype
        }
    }

    class File {
        @SerializedName("url")
        @Expose
        private var url: String? = null

        fun getUrl(): String? {
            return url;
        }
    }

    class Info {
        @SerializedName("size")
        @Expose
        private var size: Int? = null
        @SerializedName("thumbnail_file")
        @Expose
        private var thumbnailFile: ThumbnailFile? = null;

        fun getSize(): Int? {
            return size;
        }

        fun getThumbnailFile(): ThumbnailFile? {
            return thumbnailFile;
        }
    }

    class ThumbnailFile {

        @SerializedName("url")
        @Expose
        private val url: String? = null

        fun getUrl(): String? {
            return url;
        }

    }

}