package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User

/**
 * ViewModelProvider need ViewModel type to get a ViewModel instance
 * So, I cannot Interface in here to declare abstract for ViewModel
 * So, I using a Abstract Class to declare abstract for ViewModel
 */
abstract class AbstractUserViewModel : ViewModel() {
    abstract fun setUserId(userId: String);
    abstract fun getUserData(): LiveData<Resource<User>>;
    abstract fun getUserIdData(): LiveData<String>;
    abstract fun getUsers(): LiveData<Resource<List<User>>>;
    abstract fun getUsersKeywordData(): LiveData<String>;
    abstract fun setQuery(query: String);
    abstract fun setRoomIdForGetUsers(roomId: String);
    abstract fun getUsersInRoomResult(): LiveData<Resource<List<User>>>
    abstract fun getRoomViewModel() : AbstractRoomViewModel;
}