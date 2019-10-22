package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.ViewModel
import io.reactivex.Completable

abstract class AbstractRoomActivityViewModel : ViewModel() {
    abstract fun setIdForUpdateRoomNotifyCount(roomId: String): Completable;
}