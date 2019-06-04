package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodels.interfaces.AbstractHomeScreenFragmentViewModel
import javax.inject.Inject

class HomeScreenFragmentViewModel @Inject constructor(roomRepository: RoomRepository) : AbstractHomeScreenFragmentViewModel() {
    private val _typesDirectMessage = MutableLiveData<Array<Int>>();
    private val _listRoomsDirectMessage = Transformations.switchMap(_typesDirectMessage) { input -> roomRepository.loadListRoomUserJoin(input) }
    private val _typesRoomsMessage = MutableLiveData<Array<Int>>();
    private val _listRoomsMessage = Transformations.switchMap(_typesRoomsMessage) { input -> roomRepository.loadListRoomUserJoin(input) }
    override fun getListRoomDirectMessage(): LiveData<Resource<List<Room>>> {
        return _listRoomsDirectMessage;
    }

    override fun setTypesDirectMessage(types: Array<Int>) {
        _typesDirectMessage.value = types;
    }

    override fun getListRoomsMessage(): LiveData<Resource<List<Room>>> {
        return _listRoomsMessage;
    }

    override fun setTypesRoomsMessage(types: Array<Int>) {
        _typesRoomsMessage.value = types;
    }
}