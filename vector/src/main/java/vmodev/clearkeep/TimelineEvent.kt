//package vmodev.clearkeep
//
//import im.vector.analytics.Event
//import im.vector.analytics.toModel
//import vmodev.clearkeep.ContentUtils.extractUsefulTextFromReply
//
//data class TimelineEvent(
//        val root: Event,
//        val localId: Long,
//        val displayIndex: Int,
//        val senderName: String?,
//        val isUniqueDisplayName: Boolean,
//        val senderAvatar: String?,
//        val annotations: EventAnnotationsSummary? = null
//) {
//
//    val metadata = HashMap<String, Any>()
//
//    /**
//     * The method to enrich this timeline event.
//     * If you provides multiple data with the same key, only first one will be kept.
//     * @param key the key to associate data with.
//     * @param data the data to enrich with.
//     */
//    fun enrichWith(key: String?, data: Any?) {
//        if (key == null || data == null) {
//            return
//        }
//        if (!metadata.containsKey(key)) {
//            metadata[key] = data
//        }
//    }
//
//    fun getDisambiguatedDisplayName(): String {
//        return if (isUniqueDisplayName) {
//            senderName
//        } else {
//            senderName?.let { name ->
//                "$name (${root.senderId})"
//            }
//        }
//                ?: root.senderId
//                ?: ""
//    }
//
//    /**
//     * Get the metadata associated with a key.
//     * @param key the key to get the metadata
//     * @return the metadata
//     */
//    inline fun <reified T> getMetadata(key: String): T? {
//        return metadata[key] as T?
//    }
//
//    fun isEncrypted(): Boolean {
//        // warning: Do not use getClearType here
//        return EventType.ENCRYPTED == root.type
//    }
//}
//
///**
// * Get last MessageContent, after a possible edition
// */
//fun TimelineEvent.getLastMessageContent(): MessageContent? = annotations?.editSummary?.aggregatedContent?.toModel()
//        ?: root.getClearContent().toModel()
//
//
//fun TimelineEvent.getTextEditableContent(): String? {
//    val originalContent = root.getClearContent().toModel<MessageContent>() ?: return null
//    val isReply = originalContent.isReply() || root.content.toModel<EncryptedEventContent>()?.relatesTo?.inReplyTo?.eventId != null
//    val lastContent = getLastMessageContent()
//    return if (isReply) {
//        return extractUsefulTextFromReply(lastContent?.body ?: "")
//    } else {
//        lastContent?.body ?: ""
//    }
//}