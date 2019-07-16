package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractSignUpFragmentViewModel
import javax.inject.Inject

class SignUpFragmentViewModel @Inject constructor(userRepository: UserRepository) : AbstractSignUpFragmentViewModel() {

    private val _setDataForForRegister = MutableLiveData<RegisterObject>();
    private val _registerResult = Transformations.switchMap(_setDataForForRegister) { input -> userRepository.register(input.username, input.email, input.password) }

    override fun setDataForRegister(username: String, email: String, password: String) {
        _setDataForForRegister.value = RegisterObject(username, email, password);
    }

    override fun getRegisterResult(): LiveData<Resource<String>> {
        return _registerResult;
    }
}