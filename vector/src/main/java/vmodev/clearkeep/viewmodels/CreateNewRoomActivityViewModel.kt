package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodels.interfaces.AbstractCreateNewRoomActivityViewModel
import javax.inject.Inject

class CreateNewRoomActivityViewModel @Inject constructor(roomRepository: RoomRepository) : AbstractCreateNewRoomActivityViewModel() {
    private val _setCreateNewRoomObject = MutableLiveData<CreateNewRoomObject>();
    private val _createNewRoomResult = Transformations.switchMap(_setCreateNewRoomObject) { input -> roomRepository.createNewRoom(input.name, input.topic, input.visibility) }

    override fun setCreateNewRoom(name: String, topic: String, visibility: String) {
        _setCreateNewRoomObject.value = CreateNewRoomObject(name, topic, visibility);
    }

    override fun createNewRoomResult(): LiveData<Resource<Room>> {
        return _createNewRoomResult;
    }
}