package vmodev.clearkeep.repositories.interfaces

import android.arch.lifecycle.LiveData
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
    fun updateOrCreateNewUserFromRemote(roomId: String) : LiveData<Resource<List<User>>>;
}