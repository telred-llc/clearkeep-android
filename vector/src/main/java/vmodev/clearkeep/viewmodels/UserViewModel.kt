package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserViewModel
import javax.inject.Inject

class UserViewModel @Inject constructor(userRepository: UserRepository) : AbstractUserViewModel() {
    private val _userId = MutableLiveData<String>()
    private val user: LiveData<Resource<User>> = Transformations.switchMap(_userId) { input ->
        userRepository.loadUser(input)
    }

    override fun setUserId(userId: String) {
        if (_userId.value != userId) {
            _userId.value = userId;
        }
    }

    override fun getUserData(): LiveData<Resource<User>> {
        return user;
    }

    override fun getUserIdData(): LiveData<String> {
        return _userId;
    }
}