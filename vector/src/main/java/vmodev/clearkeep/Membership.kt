//package vmodev.clearkeep
//
//import com.squareup.moshi.Json
//
//enum class Membership(val value: String) {
//
//    NONE("none"),
//
//    @Json(name = "invite")
//    INVITE("invite"),
//
//    @Json(name = "join")
//    JOIN("join"),
//
//    @Json(name = "knock")
//    KNOCK("knock"),
//
//    @Json(name = "leave")
//    LEAVE("leave"),
//
//    @Json(name = "ban")
//    BAN("ban");
//
//    fun isLeft(): Boolean {
//        return this == KNOCK || this == LEAVE || this == BAN
//    }
//
//}
