package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room

abstract class AbstractCreateNewRoomActivityViewModel : ViewModel() {
    abstract fun setCreateNewRoom(name: String, topic: String, visibility: String);
    abstract fun createNewRoomResult(): LiveData<Resource<Room>>;

    data class CreateNewRoomObject(val name: String, val topic: String, val visibility: String);
}