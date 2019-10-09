package vmodev.clearkeep.ultis

import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.RoomState
import vmodev.clearkeep.viewmodelobjects.Room

fun RoomState.toRoomInvite(session: MXSession?): Room? {
    if (session != null){
        return Room(id = this.roomId, userCreated = null, avatarUrl = if (this.avatarUrl.isNullOrEmpty()) null else this.avatarUrl.matrixUrlToRealUrl(session)
                , encrypted = if (this.isEncrypted()) 0x01 else 0x00, highlightCount = this.highlightCount, name = this.name, notifyCount = this.notificationCount
                , messageId = null, notificationState = 0x0, topic = this.topic, type = 2 or 64
                , version = 0)
    }
    return null
}

fun RoomState.toRoomCreate(session: MXSession?): Room? {
    if (session != null){
        return Room(id = this.roomId, userCreated = null, avatarUrl = if (this.avatarUrl.isNullOrEmpty()) null else this.avatarUrl.matrixUrlToRealUrl(session)
                , encrypted = if (this.isEncrypted()) 0x01 else 0x00, highlightCount = this.highlightCount, name = this.name, notifyCount = this.notificationCount
                , messageId = null, notificationState = 0x0, topic = this.topic, type = 2
                , version = 0)
    }
    return null
}