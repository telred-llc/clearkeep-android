//package vmodev.clearkeep
//
//import com.squareup.moshi.Json
//import com.squareup.moshi.JsonClass
//
//@JsonClass(generateAdapter = true)
//data class EncryptedEventContent(
//
//        /**
//         * the used algorithm
//         */
//        @Json(name = "algorithm")
//        val algorithm: String? = null,
//
//        /**
//         * The encrypted event
//         */
//        @Json(name = "ciphertext")
//        val ciphertext: String? = null,
//
//        /**
//         * The device id
//         */
//        @Json(name = "device_id")
//        val deviceId: String? = null,
//
//        /**
//         * the sender key
//         */
//        @Json(name = "sender_key")
//        val senderKey: String? = null,
//
//        /**
//         * The session id
//         */
//        @Json(name = "session_id")
//        val sessionId: String? = null,
//
//        //Relation context is in clear in encrypted message
//        @Json(name = "m.relates_to") val relatesTo: RelationDefaultContent? = null
//)