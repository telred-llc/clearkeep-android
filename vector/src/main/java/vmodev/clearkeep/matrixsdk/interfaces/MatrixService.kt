package vmodev.clearkeep.matrixsdk.interfaces

import android.arch.lifecycle.LiveData
import io.reactivex.Observable
import org.matrix.androidsdk.crypto.data.ImportRoomKeysResult
import org.matrix.androidsdk.data.Room
import vmodev.clearkeep.ultis.ListRoomAndRoomUserJoinReturn
import vmodev.clearkeep.ultis.RoomAndRoomUserJoin
import vmodev.clearkeep.viewmodelobjects.KeyBackup
import vmodev.clearkeep.viewmodelobjects.Signature
import vmodev.clearkeep.viewmodelobjects.User
import java.io.InputStream

public interface MatrixService {
    fun getListDirectMessageConversation(): LiveData<List<Room>>;
    fun getListRoomConversation(): LiveData<List<Room>>;
    fun getListFavouriteConversation(): LiveData<List<Room>>;
    fun getListContact(): LiveData<List<Room>>;
    fun getUser(): Observable<User>;
    fun getListRoom(filters: Array<Int>): Observable<List<vmodev.clearkeep.viewmodelobjects.Room>>;
    fun getListRoomAndAddUser(filters: Array<Int>): Observable<ListRoomAndRoomUserJoinReturn>;
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
    fun getUsersInRoomAndAddToRoomUserJoin(roomId: String): Observable<RoomAndRoomUserJoin>;
    fun updateUser(name: String, avatar: InputStream?): Observable<User>;
    fun updateUser(name: String): Observable<String>;
    fun updateUser(avatar: InputStream): Observable<String>;
    fun exportNewBackupKey(passphrase: String): Observable<String>;
    fun sendTextMessage(roomId: String, content: String): Observable<Int>;
    fun getListFileInRoom(roomId: String): Observable<List<String>>;
    fun getListSignature(id: String): Observable<List<Signature>>
    fun exportRoomKey(passphrase: String): Observable<String>;
    fun getKeyBackUpData(userId: String): Observable<KeyBackup>;
    fun restoreBackupFromPassphrase(password: String): Observable<ImportRoomKeysResult>;
    fun restoreBackupKeyFromRecoveryKey(key: String): Observable<ImportRoomKeysResult>;
    fun getAuthDataAsMegolmBackupAuthData(): Observable<String>;
    fun deleteKeyBackup(userId: String): Observable<KeyBackup>;
    fun checkNeedBackupWhenSignOut(): Observable<Int>;
}