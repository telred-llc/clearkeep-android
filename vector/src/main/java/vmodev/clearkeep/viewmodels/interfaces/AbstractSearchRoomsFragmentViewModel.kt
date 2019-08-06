package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room

abstract class AbstractSearchRoomsFragmentViewModel : ViewModel() {
    abstract fun setQueryForSearch(query: String);
    abstract fun getRoomSearchResult(): LiveData<Resource<List<Room>>>
}