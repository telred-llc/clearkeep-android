package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room

abstract class AbstractRoomViewModel : ViewModel() {
    abstract fun setFilter(filter : Int);
    abstract fun getRoomsData() : LiveData<Resource<List<Room>>>
    abstract fun getFilterData() : LiveData<Int>;
}