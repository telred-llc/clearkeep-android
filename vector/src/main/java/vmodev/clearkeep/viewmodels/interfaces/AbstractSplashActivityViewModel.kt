package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room

abstract class AbstractSplashActivityViewModel : ViewModel() {
    abstract fun getAllRoomResult(): LiveData<Resource<List<Room>>>;
    abstract fun setFiltersForGetAllRoom(filters: Array<Int>);
}