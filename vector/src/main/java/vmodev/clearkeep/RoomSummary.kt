//package vmodev.clearkeep
//
//data class RoomSummary(
//        val roomId: String,
//        val displayName: String = "",
//        val topic: String = "",
//        val avatarUrl: String = "",
//        val isDirect: Boolean = false,
//        val latestEvent: TimelineEvent? = null,
//        val otherMemberIds: List<String> = emptyList(),
//        val notificationCount: Int = 0,
//        val highlightCount: Int = 0,
//        val tags: List<RoomTag> = emptyList(),
//        val membership: Membership = Membership.NONE,
//        val versioningState: VersioningState = VersioningState.NONE
//) {
//
//    val isVersioned: Boolean
//        get() = versioningState != VersioningState.NONE
//}