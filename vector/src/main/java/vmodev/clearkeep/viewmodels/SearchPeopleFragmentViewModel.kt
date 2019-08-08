package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.databases.AbstractUserDao
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchPeopleFragmentViewModel
import javax.inject.Inject

class SearchPeopleFragmentViewModel @Inject constructor(userRepository: UserRepository) : AbstractSearchPeopleFragmentViewModel() {
    private val _setQueryForSearch = MutableLiveData<String>();
    private val searchResult = Transformations.switchMap(_setQueryForSearch) { input -> userRepository.findUserFromNetwork(input)}
    override fun setQueryForSearch(query: String) {
        if (_setQueryForSearch.value != query)
            _setQueryForSearch.value = query;
    }

    override fun getSearchResult(): LiveData<Resource<List<User>>> {
        return searchResult;
    }
}