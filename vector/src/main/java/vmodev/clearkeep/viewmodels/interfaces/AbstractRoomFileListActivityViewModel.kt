package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractRoomFileListActivityViewModel : ViewModel() {
    abstract fun setRoomIdForGetListFile(roomId: String);
    abstract fun getListFileResult(): LiveData<Resource<List<String>>>
}