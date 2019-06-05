package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room

abstract class AbstractHomeScreenFragmentViewModel : ViewModel() {
    abstract fun getListRoomDirectMessage(): LiveData<Resource<List<Room>>>
    abstract fun setTypesDirectMessage(types: Array<Int>)
    abstract fun getListRoomsMessage(): LiveData<Resource<List<Room>>>
    abstract fun setTypesRoomsMessage(types: Array<Int>)
}