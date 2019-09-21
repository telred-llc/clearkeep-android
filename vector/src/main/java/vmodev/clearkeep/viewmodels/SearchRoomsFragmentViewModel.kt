package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.RoomUserJoinRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchRoomsFragmentViewModel
import javax.inject.Inject

class SearchRoomsFragmentViewModel @Inject constructor(roomRepository: RoomRepository, roomUserJoinRepository: RoomUserJoinRepository) : AbstractSearchRoomsFragmentViewModel() {

    private val _query = MutableLiveData<String>();
    private val _searchRoomInviteResult = Transformations.switchMap(_query) { input -> roomUserJoinRepository.getListRoomListUserWithListRoomId(listOf(65, 66), input) }
    private val _searchRoomFavouriteResult = Transformations.switchMap(_query) { input -> roomUserJoinRepository.getListRoomListUserWithListRoomId(listOf(129, 130), input) }
    private val _searchRoomNormalResult = Transformations.switchMap(_query) { input -> roomUserJoinRepository.getListRoomListUserWithListRoomId(listOf(1, 2), input) }

    override fun setQueryForSearch(query: String) {
        if (_query.value != query)
            _query.value = query;
    }

    override fun getRoomInviteSearchResult(): LiveData<Resource<List<RoomListUser>>> {
        return _searchRoomInviteResult;
    }

    override fun getRoomFavouriteSearchResult(): LiveData<Resource<List<RoomListUser>>> {
        return _searchRoomFavouriteResult;
    }

    override fun getRoomNormalSearchResult(): LiveData<Resource<List<RoomListUser>>> {
        return _searchRoomNormalResult;
    }
}