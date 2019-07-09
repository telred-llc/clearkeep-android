package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
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