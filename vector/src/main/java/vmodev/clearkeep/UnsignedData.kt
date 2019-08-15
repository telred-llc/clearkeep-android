//package vmodev.clearkeep
//
//import com.squareup.moshi.Json
//import com.squareup.moshi.JsonClass
//import im.vector.analytics.Event
//
//@JsonClass(generateAdapter = true)
//data class UnsignedData(
//        @Json(name = "age") val age: Long?,
//        @Json(name = "redacted_because") val redactedEvent: Event? = null,
//        @Json(name = "transaction_id") val transactionId: String? = null,
//        @Json(name = "prev_content") val prevContent: Map<String, Any>? = null,
//        @Json(name = "m.relations") val relations: AggregatedRelations? = null)