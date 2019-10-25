package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.RoomUserJoinRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractCreateNewCallActivityViewModel
import javax.inject.Inject

class CreateNewCallActivityViewModel @Inject constructor(private val userRepository: UserRepository, roomRepository: RoomRepository, private val roomUserJoinRepository: RoomUserJoinRepository) : AbstractCreateNewCallActivityViewModel() {

    private val _setQuery = MutableLiveData<String>();
    private val _getUsersByQueryResult = Transformations.switchMap(_setQuery) { input -> userRepository.findUserFromNetwork(input) }
    private val _setCreateNewRoom = MutableLiveData<CreateNewRoom>();
    private val _getCreateNewRoomResult = Transformations.switchMap(_setCreateNewRoom) { input -> roomRepository.createNewRoom(input.name, input.topic, input.visibility) }

    override fun setQuery(query: String) {
        if (_setQuery.value != query)
            _setQuery.value = query;
    }

    override fun getUsersByQueryResult(): LiveData<Resource<List<User>>> {
        return _getUsersByQueryResult;
    }

    override fun setCreateNewRoom(name: String, topic: String, visibility: String) {
        _setCreateNewRoom.value = CreateNewRoom(name, topic, visibility);
    }

    override fun getCreateNewRoomResult(): LiveData<Resource<Room>> {
        return _getCreateNewRoomResult;
    }

    override fun getListUserSuggested(type: Int, userId: String): LiveData<Resource<List<User>>> {
        return roomUserJoinRepository.getListUserSuggested(type, userId)
    }
}