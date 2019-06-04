package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodels.interfaces.AbstractFavouritesFragmentViewModel
import javax.inject.Inject

class FavouritesFragmentViewModel @Inject constructor(roomRepository: RoomRepository) : AbstractFavouritesFragmentViewModel() {
    private val _listType = MutableLiveData<Array<Int>>();
    private val listRoomByType = Transformations.switchMap(_listType) { input -> roomRepository.loadListRoomUserJoin(input, 1) }
    private val _removeFromFavourite = MutableLiveData<String>();
    private val removeFromFavouriteResult = Transformations.switchMap(_removeFromFavourite) { input -> roomRepository.removeFromFavourite(input) }
    private val _leaveRoom = MutableLiveData<String>();
    private val leaveRoomResult = Transformations.switchMap(_leaveRoom) { input -> roomRepository.leaveRoom(input) }

    override fun getListRoomByType(): LiveData<Resource<List<Room>>> {
        return listRoomByType;
    }

    override fun setListType(types: Array<Int>) {
        _listType.value = types;
    }

    override fun getRemoveFromFavouriteResult(): LiveData<Resource<Room>> {
        return removeFromFavouriteResult;
    }

    override fun setRemoveFromFavourite(roomId: String) {
        _removeFromFavourite.value = roomId;
    }

    override fun getLeaveRoom(): LiveData<Resource<String>> {
        return leaveRoomResult;
    }

    override fun setLeaveRoom(roomId: String) {
        _leaveRoom.value = roomId;
    }
}