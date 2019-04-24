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
    private val _filters = MutableLiveData<Array<Int>>();
    private val _id = MutableLiveData<String>();
    private val rooms: LiveData<Resource<List<Room>>> = Transformations.switchMap(_filters) { input ->
        roomRepository.loadListRoom(input);
    }
    private val room: LiveData<Resource<Room>> = Transformations.switchMap(_id) { input -> roomRepository.loadRoom(input) };

    override fun setFilter(filters: Array<Int>) {
        _filters.value = filters;
    }

    override fun getRoomsData(): LiveData<Resource<List<Room>>> {
        return rooms;
    }

    override fun getFilterData(): LiveData<Array<Int>> {
        return _filters;
    }

    override fun getRoom(): LiveData<Resource<Room>> {
        return room;
    }

    override fun setRoomId(id: String) {
        if (_id.value != id)
            _id.value = id;
    }
}