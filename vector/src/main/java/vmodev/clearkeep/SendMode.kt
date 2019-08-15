//package vmodev.clearkeep
//
//import im.vector.analytics.Event
//
//
//sealed class SendMode {
//    object REGULAR : SendMode()
//    data class QUOTE(val timelineEvent: TimelineEvent) : SendMode()
//    data class EDIT(val timelineEvent: TimelineEvent) : SendMode()
//    data class REPLY(val timelineEvent: TimelineEvent) : SendMode()
//}
//
//data class RoomDetailViewState(
//        val roomId: String,
//        val eventId: String?,
//        val timeline: Timeline? = null,
//        val asyncInviter: Async<User> = Uninitialized,
//        val asyncRoomSummary: Async<RoomSummary> = Uninitialized,
//        val sendMode: SendMode = SendMode.REGULAR,
//        val isEncrypted: Boolean = false,
//        val tombstoneEvent: Event? = null,
//        val tombstoneEventHandling: Async<String> = Uninitialized
//)  {
//
////    constructor(args: RoomDetailArgs) : this(roomId = args.roomId, eventId = args.eventId)
//
//}