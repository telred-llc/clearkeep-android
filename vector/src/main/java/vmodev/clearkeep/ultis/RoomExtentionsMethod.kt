package vmodev.clearkeep.ultis

import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room

fun MXSession.getJoinedRoom(): List<Room> {
    return this.dataHandler.store!!.rooms
            .filter {
                val isJoined = it.isJoined
                val tombstoneContent = it.state.roomTombstoneContent
                val redirectRoom = if (tombstoneContent?.replacementRoom != null) {
                    this.dataHandler.getRoom(tombstoneContent.replacementRoom)
                } else {
                    null
                }
                val isVersioned = redirectRoom?.isJoined
                        ?: false
                isJoined && !isVersioned && !it.isConferenceUserRoom
            }
}