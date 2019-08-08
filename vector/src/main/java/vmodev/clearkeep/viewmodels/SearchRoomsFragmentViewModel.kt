package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.RoomUserJoinRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchRoomsFragmentViewModel
import javax.inject.Inject

class SearchRoomsFragmentViewModel @Inject constructor(roomRepository: RoomRepository, roomUserJoinRepository: RoomUserJoinRepository) : AbstractSearchRoomsFragmentViewModel() {

    private val _query = MutableLiveData<String>();
    private val _roomIds = MutableLiveData<List<String>>();
    private val _searchResult = Transformations.switchMap(_query) { input -> roomRepository.findListRoomWithText(input) }
    private val _searchRoomIdsResult = Transformations.switchMap(_roomIds){input ->  roomUserJoinRepository.getListRoomListUserWithListRoomId(input)}

    override fun setQueryForSearch(query: String) {
        if (_query.value != query)
            _query.value = query;
    }

    override fun getRoomSearchResult(): LiveData<Resource<List<RoomListUser>>> {
        return _searchRoomIdsResult;
    }

    override fun getListRoomIdResult(): LiveData<Resource<List<String>>> {
        return _searchResult;
    }

    override fun setListRoomId(roomIds: List<String>) {
        _roomIds.value = roomIds;
    }
}