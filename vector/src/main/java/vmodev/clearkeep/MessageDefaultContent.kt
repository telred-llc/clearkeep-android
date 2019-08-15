//package vmodev.clearkeep
//
//import com.squareup.moshi.Json
//import com.squareup.moshi.JsonClass
//import im.vector.analytics.Content
//import vmodev.clearkeep.jsonmodels.MessageContent
//
//@JsonClass(generateAdapter = true)
//data class MessageDefaultContent(
//        @Json(name = "msgtype") override val type: String,
//        @Json(name = "body") override val body: String,
//        @Json(name = "m.relates_to") override val relatesTo: RelationDefaultContent? = null,
//        @Json(name = "m.new_content") override val newContent: Content? = null
//) : MessageContentEdit
//
//interface MessageContent {
//    val type: String
//    val body: String
//    val relatesTo: RelationDefaultContent?
//    val newContent: Content?
//}
//
//
//fun MessageContentEdit?.isReply(): Boolean {
//    return this?.relatesTo?.inReplyTo?.eventId != null
//}