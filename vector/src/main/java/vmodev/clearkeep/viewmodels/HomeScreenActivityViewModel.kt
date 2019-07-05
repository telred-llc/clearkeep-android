package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractHomeScreenActivityViewModel
import javax.inject.Inject

class HomeScreenActivityViewModel @Inject constructor(userRepository: UserRepository, roomRepository: RoomRepository) : AbstractHomeScreenActivityViewModel() {
    private val _userId = MutableLiveData<String>();
    private val _filters = MutableLiveData<Array<Int>>();
    private val _filtersFavourite = MutableLiveData<Array<Int>>();

    private val userById = Transformations.switchMap(_userId) { input -> userRepository.loadUser(input) }
    private val listRoomByType = Transformations.switchMap(_filters) { input -> roomRepository.loadListRoomUserJoin(input) }
    private val _getListFavouriteResult = Transformations.switchMap(_filtersFavourite) { input -> roomRepository.loadListRoomUserJoin(input) }

    override fun getUserById(): LiveData<Resource<User>> {
        return userById;
    }

    override fun getListRoomByType(): LiveData<Resource<List<Room>>> {
        return listRoomByType;
    }

    override fun setValueForUserById(userId: String) {
        _userId.value = userId;
    }

    override fun setValueForListRoomType(filters: Array<Int>) {
        _filters.value = filters;
    }

    override fun setValueForListRoomTypeFavourite(filters: Array<Int>) {
        _filtersFavourite.value = filters;
    }

    override fun getListRoomTypeFavouriteResult(): LiveData<Resource<List<Room>>> {
        return _getListFavouriteResult;
    }
}