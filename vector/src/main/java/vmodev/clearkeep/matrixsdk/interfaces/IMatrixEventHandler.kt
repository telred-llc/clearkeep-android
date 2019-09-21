package vmodev.clearkeep.matrixsdk.interfaces

import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.listeners.MXEventListener

interface IMatrixEventHandler {
    fun getMXEventListener(mxSession: MXSession): MXEventListener;

    companion object {
        const val M_ROOM_MEMBER = "m.room.member";
        const val M_ROOM_CREATE = "m.room.create";
        const val M_ROOM_POWER_LEVELS = "m.room.power_level";
        const val M_ROOM_JOIN_RULES = "m.room.join_rules";
        const val M_ROOM_HISTORY_VISIBILITY = "m.room.history_visibility";
        const val M_ROOM_GUEST_ACCESS = "m.room.guest_access";
        const val M_ROOM_ENCRYPTION = "m.room.encryption";
        const val M_ROOM_NAME = "m.room.name";
    }
}