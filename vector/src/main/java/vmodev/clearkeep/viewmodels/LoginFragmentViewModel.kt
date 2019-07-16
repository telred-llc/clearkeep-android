package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractLoginFragmentViewModel
import javax.inject.Inject

class LoginFragmentViewModel @Inject constructor(userRepository: UserRepository) : AbstractLoginFragmentViewModel() {
    private val _setUserNamePasswordForLogin = MutableLiveData<LoginObject>();
    private val _getLoginResult = Transformations.switchMap(_setUserNamePasswordForLogin) { input -> userRepository.login(input.username, input.password) }
    override fun setUserNamePasswordForLogin(username: String, password: String) {
        _setUserNamePasswordForLogin.value = LoginObject(username, password);
    }

    override fun getLoginResult(): LiveData<Resource<String>> {
        return _getLoginResult;
    }
}