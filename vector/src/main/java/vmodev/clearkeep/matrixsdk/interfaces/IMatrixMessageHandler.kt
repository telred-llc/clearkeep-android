package vmodev.clearkeep.matrixsdk.interfaces

import io.reactivex.Observable
import vmodev.clearkeep.viewmodelobjects.Message

interface IMatrixMessageHandler {
    fun getHistoryMessage(): Observable<List<Message>>;
}