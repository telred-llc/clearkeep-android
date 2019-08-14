package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomListUser

abstract class AbstractSearchRoomsFragmentViewModel : ViewModel() {
    abstract fun setQueryForSearch(query: String);
    abstract fun getRoomInviteSearchResult(): LiveData<Resource<List<RoomListUser>>>
    abstract fun getRoomFavouriteSearchResult(): LiveData<Resource<List<RoomListUser>>>
    abstract fun getRoomNormalSearchResult(): LiveData<Resource<List<RoomListUser>>>
}