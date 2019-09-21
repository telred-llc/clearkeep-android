package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import vmodev.clearkeep.viewmodelobjects.*

abstract class AbstractSplashActivityViewModel : ViewModel() {
    abstract fun getAllRoomResult(filters: Array<Int>): LiveData<Resource<List<Room>>>;
    abstract fun getAllRoomResultRx(filters: Array<Int>): Observable<List<Room>>;
    abstract fun getUpdateUserResult(roomId: String): LiveData<Resource<List<User>>>
    abstract fun getUpdateRoomUserJoinResult(roomId: String, userId: String);
    abstract fun getUpdateLastMessageResult(roomId: String): Observable<Message>;
    abstract fun updateRoomLastMessage(roomId: String, messageId: String);
    abstract fun updateUsersFromRoom(roomId: String): Observable<List<User>>;
    abstract fun updateRoomUserCreated(roomId: String, userId: String);
}