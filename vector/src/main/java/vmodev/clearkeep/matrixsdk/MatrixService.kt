package vmodev.clearkeep.matrixsdk

import android.arch.lifecycle.LiveData
import io.reactivex.Observable
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room
import vmodev.clearkeep.viewmodelobjects.User

public interface MatrixService {
    fun getListDirectMessageConversation() : LiveData<List<Room>>;
    fun getListRoomConversation() : LiveData<List<Room>>;
    fun getListFavouriteConversation() : LiveData<List<Room>>;
    fun getListContact() : LiveData<List<Room>>;
    fun getUser() : Observable<User>;
    fun getListRoom(filter : Int) : Observable<List<vmodev.clearkeep.viewmodelobjects.Room>>;
}