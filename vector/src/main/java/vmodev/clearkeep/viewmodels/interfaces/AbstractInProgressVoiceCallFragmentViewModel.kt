package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room

abstract class AbstractInProgressVoiceCallFragmentViewModel : ViewModel() {
    abstract fun setRoomId(roomId : String);
    abstract fun getRoomResult() : LiveData<Resource<Room>>
}