//package vmodev.clearkeep
//
//import com.squareup.moshi.Json
//import com.squareup.moshi.JsonClass
//import im.vector.types.JsonDict
//import im.vector.types.JsonDictEdit
//
//@JsonClass(generateAdapter = true)
//data class OlmDecryptionResult(
//        /**
//         * The decrypted payload (with properties 'type', 'content')
//         */
//        @Json(name = "payload") val payload: JsonDictEdit? = null,
//
//        /**
//         * keys that the sender of the event claims ownership of:
//         * map from key type to base64-encoded key.
//         */
//        @Json(name = "keysClaimed") val keysClaimed: Map<String, String>? = null,
//
//        /**
//         * The curve25519 key that the sender of the event is known to have ownership of.
//         */
//        @Json(name = "senderKey") val senderKey: String? = null,
//
//        /**
//         * Devices which forwarded this session to us (normally empty).
//         */
//        @Json(name = "forwardingCurve25519KeyChain") val forwardingCurve25519KeyChain: List<String>? = null
//)
