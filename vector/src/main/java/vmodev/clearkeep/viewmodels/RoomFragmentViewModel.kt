package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomFragmentViewModel
import javax.inject.Inject

class RoomFragmentViewModel @Inject constructor(roomRepository: RoomRepository) : AbstractRoomFragmentViewModel() {
    private val _listType = MutableLiveData<Array<Int>>();
    private val listRoomByType = Transformations.switchMap(_listType) { input -> roomRepository.loadListRoomUserJoin(input) }
    private val _addToFavourite = MutableLiveData<String>();
    private val addToFavouriteResult = Transformations.switchMap(_addToFavourite) { input -> roomRepository.addToFavourite(input) }
    private val _leaveRoom = MutableLiveData<String>();
    private val leaveRoomResult = Transformations.switchMap(_leaveRoom) { input -> roomRepository.leaveRoom(input) }

    override fun getListRoomByType(): LiveData<Resource<List<Room>>> {
        return listRoomByType;
    }

    override fun setListType(types: Array<Int>) {
        _listType.value = types;
    }

    override fun getAddToFavouriteResult(): LiveData<Resource<Room>> {
        return addToFavouriteResult;
    }

    override fun setAddToFavourite(roomId: String) {
        _addToFavourite.value = roomId;
    }

    override fun getLeaveRoom(): LiveData<Resource<String>> {
        return leaveRoomResult;
    }

    override fun setLeaveRoom(roomId: String) {
        _leaveRoom.value = roomId;
    }
}