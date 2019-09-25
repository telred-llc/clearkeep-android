package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomFileListActivityViewModel
import javax.inject.Inject

class RoomFileListActivityViewModel @Inject constructor(roomRepository: RoomRepository) : AbstractRoomFileListActivityViewModel() {

    private val _setRoomIdForGetListFile = MutableLiveData<String>();
    private val _getListFileResult = Transformations.switchMap(_setRoomIdForGetListFile) { input -> roomRepository.getListFileInRoom(input) }

    override fun setRoomIdForGetListFile(roomId: String) {
        _setRoomIdForGetListFile.value = roomId;
    }

    override fun getListFileResult(): LiveData<Resource<List<String>>> {
        return _getListFileResult;
    }
}