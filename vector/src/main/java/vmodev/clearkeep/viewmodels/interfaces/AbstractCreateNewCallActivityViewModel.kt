package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import dagger.Module
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractCreateNewCallActivityViewModel : ViewModel() {
    abstract fun setQuery(query: String);
    abstract fun getUsersByQueryResult(): LiveData<Resource<List<User>>>;
    abstract fun setCreateNewRoom(name: String, topic: String, visibility: String);
    abstract fun getCreateNewRoomResult(): LiveData<Resource<Room>>;

    data class CreateNewRoom(val name: String, val topic: String, val visibility: String);
}