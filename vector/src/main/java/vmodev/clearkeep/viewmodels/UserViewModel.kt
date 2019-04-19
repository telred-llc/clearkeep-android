package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.matrixsdk.MatrixService
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User
import javax.inject.Inject

class UserViewModel @Inject constructor(userRepository: UserRepository) : ViewModel() {
    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _userId;

    val user: LiveData<Resource<User>> = Transformations.switchMap(_userId) { input ->
        userRepository.loadUser(input)
    }

    fun setUserId(userId: String) {
        if (_userId.value != userId) {
            _userId.value = userId;
        }
    }
}