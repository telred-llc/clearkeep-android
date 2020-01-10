package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.LoginRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractSignUpFragmentViewModel
import javax.inject.Inject

class SignUpFragmentViewModel @Inject constructor(loginRepository: LoginRepository) : AbstractSignUpFragmentViewModel() {

    private val _setDataForForRegister = MutableLiveData<RegisterObject>()
    private val _registerResult = Transformations.switchMap(_setDataForForRegister) { input -> loginRepository.register(input.username, input.email, input.password) }

    override fun setDataForRegister(username: String, email: String, password: String) {
        _setDataForForRegister.value = RegisterObject(username, email, password)
    }

    override fun getRegisterResult(): LiveData<Resource<String>> {
        return _registerResult
    }
}