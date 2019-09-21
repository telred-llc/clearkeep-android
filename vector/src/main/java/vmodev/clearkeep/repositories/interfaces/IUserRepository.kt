package vmodev.clearkeep.repositories.interfaces

import androidx.lifecycle.LiveData
import io.reactivex.Observable
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User
import java.io.InputStream

interface IUserRepository : IRepository {
    fun loadUser(userId: String): LiveData<Resource<User>>
    fun updateUser(userId: String, name: String, avatarUrl: String)
    fun findUserFromNetwork(keyword: String): LiveData<Resource<List<User>>>
    fun updateUser(userId: String, name: String, avatarImage: InputStream?): LiveData<Resource<User>>
    fun updateUserStatus(userId: String, status: Byte)
    fun getListUserInRoomFromNetwork(roomId: String): LiveData<Resource<List<User>>>
    fun updateOrCreateNewUserFromRemote(roomId: String): LiveData<Resource<List<User>>>;
    fun getUsersWithId(userIds: Array<String>): LiveData<Resource<List<User>>>
    fun getListUserInRoomFromNetworkRx(roomId: String): Observable<List<User>>;
}