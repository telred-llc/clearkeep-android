package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchRoomsFragmentViewModel
import javax.inject.Inject

class SearchRoomsFragmentViewModel @Inject constructor() : AbstractSearchRoomsFragmentViewModel() {
    override fun setQueryForSearch(query: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRoomSearchResult(): LiveData<Resource<List<Room>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}