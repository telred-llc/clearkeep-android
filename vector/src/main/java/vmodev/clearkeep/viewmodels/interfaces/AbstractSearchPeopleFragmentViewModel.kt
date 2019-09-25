package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractSearchPeopleFragmentViewModel : ViewModel() {
    abstract fun setQueryForSearch(query: String);
    abstract fun getSearchResult(): LiveData<Resource<List<User>>>
}