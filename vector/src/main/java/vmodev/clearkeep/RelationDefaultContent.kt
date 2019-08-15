//package vmodev.clearkeep
//
//import com.squareup.moshi.Json
//import com.squareup.moshi.JsonClass
//
//@JsonClass(generateAdapter = true)
//data class RelationDefaultContent(
//        @Json(name = "rel_type") override val type: String?,
//        @Json(name = "event_id") override val eventId: String?,
//        @Json(name = "m.in_reply_to") override val inReplyTo: ReplyToContent? = null
//) : RelationContent