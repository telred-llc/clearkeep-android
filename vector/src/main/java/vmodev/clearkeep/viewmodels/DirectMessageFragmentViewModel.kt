package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodels.interfaces.AbstractDirectMessageFragmentViewModel
import javax.inject.Inject

class DirectMessageFragmentViewModel @Inject constructor(roomRepository: RoomRepository) : AbstractDirectMessageFragmentViewModel() {
    private val _listType = MutableLiveData<Array<Int>>();
    private val listRoomByType = Transformations.switchMap(_listType) { input -> roomRepository.loadListRoom(input) }
    private val _addToFavourite = MutableLiveData<String>();
    private val addToFavouriteResult = Transformations.switchMap(_addToFavourite) { input -> roomRepository.addToFavourite(input) }
    private val _leaveRoom = MutableLiveData<String>();
    private val leaveRoomResult = Transformations.switchMap(_leaveRoom) { input -> roomRepository.leaveRoom(input) }
    private val _setQueryForSearchRoom = MutableLiveData<String>();
    private val _searchRoomResult = Transformations.switchMap(_setQueryForSearchRoom) { input -> roomRepository.searchRoomByDisplayName(arrayOf(1, 129), input) }

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

    override fun setQueryForSearch(query: String) {
        if (_setQueryForSearchRoom.value != query)
            _setQueryForSearchRoom.value = query;
    }

    override fun getSearchResult(): LiveData<Resource<List<RoomListUser>>> {
        return _searchRoomResult;
    }
}