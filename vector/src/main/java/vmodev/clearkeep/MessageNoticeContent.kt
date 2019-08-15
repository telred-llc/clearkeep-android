//package vmodev.clearkeep
//
//import com.squareup.moshi.Json
//import com.squareup.moshi.JsonClass
//import im.vector.analytics.Content
//
//@JsonClass(generateAdapter = true)
//data class MessageNoticeContent(
//        @Json(name = "msgtype") override val type: String,
//        @Json(name = "body") override val body: String,
//        @Json(name = "format") val format: String? = null,
//        @Json(name = "formatted_body") val formattedBody: String? = null,
//        @Json(name = "m.relates_to") override val relatesTo: RelationDefaultContent? = null,
//        @Json(name = "m.new_content") override val newContent: Content? = null
//) : MessageContentEdit