package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import javax.inject.Inject

class RoomViewModel @Inject constructor(roomRepository: RoomRepository) : AbstractRoomViewModel() {
    private val _filter = MutableLiveData<Int>();
    private val rooms: LiveData<Resource<List<Room>>> = Transformations.switchMap(_filter) { input ->
        roomRepository.loadListRoom(input);
    }

    override fun setFilter(filter: Int) {
        if (_filter.value != filter)
            _filter.value = filter;
    }

    override fun getRoomsData(): LiveData<Resource<List<Room>>> {
        return rooms;
    }

    override fun getFilterData(): LiveData<Int> {
        return _filter;
    }
}