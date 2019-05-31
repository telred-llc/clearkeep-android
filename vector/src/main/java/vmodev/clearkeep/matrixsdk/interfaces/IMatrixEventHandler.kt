package vmodev.clearkeep.matrixsdk.interfaces

import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.listeners.MXEventListener

interface IMatrixEventHandler {
    fun getMXEventListener(mxSession: MXSession) : MXEventListener;
}