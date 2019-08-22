package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import vmodev.clearkeep.viewmodelobjects.*

abstract class AbstractSplashActivityViewModel : ViewModel() {
    abstract fun getAllRoomResult(filters: Array<Int>): LiveData<Resource<List<Room>>>;
    abstract fun getUpdateUserResult(roomId: String): LiveData<Resource<List<User>>>
    abstract fun getUpdateRoomUserJoinResult(roomId: String, userId: String);
    abstract fun getUpdateLastMessageResult(roomId: String): Observable<Message>;
    abstract fun updateRoomLastMessage(roomId: String,messageId: String);
    abstract fun updateUsersFromRoom(roomId: String) : Observable<List<User>>;
}