package vmodev.clearkeep.matrixsdk

import android.arch.lifecycle.LiveData
import io.reactivex.Observable
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room
import vmodev.clearkeep.viewmodelobjects.MessageSearchText
import vmodev.clearkeep.viewmodelobjects.User

public interface MatrixService {
    fun getListDirectMessageConversation(): LiveData<List<Room>>;
    fun getListRoomConversation(): LiveData<List<Room>>;
    fun getListFavouriteConversation(): LiveData<List<Room>>;
    fun getListContact(): LiveData<List<Room>>;
    fun getUser(): Observable<User>;
    fun getListRoom(filters: Array<Int>): Observable<List<vmodev.clearkeep.viewmodelobjects.Room>>;
    fun getListRoomWithTwoFilter(filterOne: Int, filterTwo: Int): Observable<List<vmodev.clearkeep.viewmodelobjects.Room>>;
    fun getRoomWithId(id: String): Observable<vmodev.clearkeep.viewmodelobjects.Room>;
    fun joinRoom(id: String): Observable<vmodev.clearkeep.viewmodelobjects.Room>;
    fun leaveRoom(id: String): Observable<String>;
    fun findListUser(keyword: String): Observable<List<User>>;
    fun <T> findListMessageText(keyword: String, typeOfClass: Class<T>): Observable<List<T>>;
    fun createNewDirectMessage(userId: String): Observable<vmodev.clearkeep.viewmodelobjects.Room>;
    fun createNewRoom(name: String, topic: String, visibility: String): Observable<vmodev.clearkeep.viewmodelobjects.Room>
    fun inviteUsersToRoom(roomId: String, userIds: List<String>): Observable<vmodev.clearkeep.viewmodelobjects.Room>;
    fun findMediaFiles(keyword: String): Observable<List<String>>;
    fun addToFavourite(roomId: String): Observable<vmodev.clearkeep.viewmodelobjects.Room>;
    fun removeFromFavourite(roomId: String): Observable<vmodev.clearkeep.viewmodelobjects.Room>;
    fun getUsersInRoom(roomId: String): Observable<List<User>>;
}