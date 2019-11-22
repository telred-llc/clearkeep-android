package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomUserJoinRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchPeopleFragmentViewModel
import javax.inject.Inject

class SearchPeopleFragmentViewModel @Inject constructor(userRepository: UserRepository, private val roomUserJoinRepository: RoomUserJoinRepository) : AbstractSearchPeopleFragmentViewModel() {

    private val _setQueryForSearch = MutableLiveData<String>();
    private val searchResult = Transformations.switchMap(_setQueryForSearch) { input -> userRepository.findUserFromNetwork(input)}
    override fun setQueryForSearch(query: String) {
        if (_setQueryForSearch.value != query)
            _setQueryForSearch.value = query;
    }

    override fun getSearchResult(): LiveData<Resource<List<User>>> {
        return searchResult;
    }

    override fun getListUserMatrixContact(typeOne: Int, typeTwo: Int, userID: String): LiveData<Resource<List<User>>> {
        return roomUserJoinRepository.getListUserMatrixContact(typeOne,typeTwo, userID)
    }
}