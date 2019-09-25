package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractEditProfileActivityViewModel
import java.io.InputStream
import javax.inject.Inject

class EditProfileActivityViewModel @Inject constructor(private val userRepository: UserRepository) : AbstractEditProfileActivityViewModel() {
    private val _userId = MutableLiveData<String>();
    private val _getUserResult = Transformations.switchMap(_userId) { input -> userRepository.loadUser(input) }
    private val _updateUser = MutableLiveData<UpdateUser>();
    private val _getUpdateUserResult = Transformations.switchMap(_updateUser) { input -> userRepository.updateUser(input.userId, input.name, input.avatarImage) }

    override fun getUserResult(): LiveData<Resource<User>> {
        return _getUserResult;
    }

    override fun setUserId(userId: String) {
        _userId.value = userId;
    }

    override fun getUserUpdateResult(): LiveData<Resource<User>> {
        return _getUpdateUserResult;
    }

    override fun setUpdateUser(userId: String, name: String, avatarImage: InputStream?) {
        _updateUser.value = UpdateUser(userId, name, avatarImage);
    }
}