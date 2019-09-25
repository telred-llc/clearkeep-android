package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User
import java.io.InputStream

abstract class AbstractEditProfileActivityViewModel : ViewModel() {
    abstract fun getUserResult(): LiveData<Resource<User>>;
    abstract fun setUserId(userId: String);
    abstract fun getUserUpdateResult(): LiveData<Resource<User>>
    abstract fun setUpdateUser(userId: String, name: String, avatarImage: InputStream?);

    data class UpdateUser(val userId: String, val name: String, val avatarImage: InputStream?)
}