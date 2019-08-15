//package vmodev.clearkeep
//
//enum class SendState {
//    UNKNOWN,
//    // the event has not been sent
//    UNSENT,
//    // the event is encrypting
//    ENCRYPTING,
//    // the event is currently sending
//    SENDING,
//    // the event has been sent
//    SENT,
//    // the event has been received from server
//    SYNCED,
//    // The event failed to be sent
//    UNDELIVERED,
//    // the event failed to be sent because some unknown devices have been found while encrypting it
//    FAILED_UNKNOWN_DEVICES;
//
//    fun isSent(): Boolean {
//        return this == SENT || this == SYNCED
//    }
//
//    fun hasFailed(): Boolean {
//        return this == UNDELIVERED || this == FAILED_UNKNOWN_DEVICES
//    }
//
//    fun isSending(): Boolean {
//        return this == UNSENT || this == ENCRYPTING || this == SENDING
//    }
//
//}
