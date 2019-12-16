package vmodev.clearkeep.viewmodelobjects

import com.google.gson.annotations.SerializedName

data class Resource<out T>(@SerializedName("status") val status: Status, @SerializedName("data") val data: T?, @SerializedName("message") val message: String?) {
    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String?, data : T?) : Resource<T>{
            return Resource(Status.ERROR, data, message)
        }

        fun <T> loading(data : T?) : Resource<T>{
            return Resource(Status.LOADING, data, null)
        }
    }
}