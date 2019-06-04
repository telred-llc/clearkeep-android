package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodels.interfaces.AbstractSplashActivityViewModel
import javax.inject.Inject

class SplashActivityViewModel @Inject constructor(roomRepository: RoomRepository) : AbstractSplashActivityViewModel() {
    private val _filters = MutableLiveData<Array<Int>>();
    private val _getAllRoomResult = Transformations.switchMap(_filters) { input -> roomRepository.updateAllRoomWhenStartApp(input) }

    override fun getAllRoomResult(): LiveData<Resource<List<Room>>> {
        return _getAllRoomResult;
    }

    override fun setFiltersForGetAllRoom(filters: Array<Int>) {
        _filters.value = filters;
    }
}