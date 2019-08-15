//package vmodev.clearkeep
//
//import im.vector.analytics.Content
//
//interface MessageContentEdit {
//    val type: String
//    val body: String
//    val relatesTo: RelationDefaultContent?
//    val newContent: Content?
//}
//
//
//fun MessageContent?.isReply(): Boolean {
//    return this?.relatesTo?.inReplyTo?.eventId != null
//}
