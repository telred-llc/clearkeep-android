package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractViewUserProfileActivityViewModel : ViewModel() {
    abstract fun getUserResult(): LiveData<Resource<User>>;
    abstract fun createNewDirectChatResult(): LiveData<Resource<Room>>;
    abstract fun setGetUser(userId: String);
    abstract fun setUserIdForCreateNewChat(userId: String);
}