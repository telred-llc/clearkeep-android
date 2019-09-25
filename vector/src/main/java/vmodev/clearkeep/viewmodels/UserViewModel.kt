package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomUserJoinRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserViewModel
import javax.inject.Inject

class UserViewModel @Inject constructor(userRepository: UserRepository, roomUserJoinRepository: RoomUserJoinRepository) : AbstractUserViewModel() {
    private val _userId = MutableLiveData<String>()
    private val _query = MutableLiveData<String>();
    private val _roomIdForGetUsers = MutableLiveData<String>();

    private val user: LiveData<Resource<User>> = Transformations.switchMap(_userId) { input ->
        userRepository.loadUser(input)
    }
    private val users: LiveData<Resource<List<User>>> = Transformations.switchMap(_query) { input ->
        userRepository.findUserFromNetwork(input);
    }

    private val usersInRoom: LiveData<Resource<List<User>>> = Transformations.switchMap(_roomIdForGetUsers) { input ->
        userRepository.getListUserInRoomFromNetwork(input);
    }

    override fun setUserId(userId: String) {
        if (_userId.value != userId) {
            _userId.value = userId;
        }
    }

    override fun getUserData(): LiveData<Resource<User>> {
        return user;
    }

    override fun getUserIdData(): LiveData<String> {
        return _userId;
    }

    override fun getUsers(): LiveData<Resource<List<User>>> {
        return users;
    }

    override fun getUsersKeywordData(): LiveData<String> {
        return _query;
    }

    override fun setQuery(query: String) {
        if (_query.value != query)
            _query.value = query;
    }

    override fun setRoomIdForGetUsers(roomId: String) {
        _roomIdForGetUsers.value = roomId;
    }

    override fun getUsersInRoomResult(): LiveData<Resource<List<User>>> {
        return usersInRoom;
    }

    override fun getRoomViewModel(): AbstractRoomViewModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}