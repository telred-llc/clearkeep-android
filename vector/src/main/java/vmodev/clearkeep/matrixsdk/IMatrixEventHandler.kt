package vmodev.clearkeep.matrixsdk

import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.listeners.MXEventListener

interface IMatrixEventHandler {
    fun getMXEventListener(mxSession: MXSession) : MXEventListener;
}