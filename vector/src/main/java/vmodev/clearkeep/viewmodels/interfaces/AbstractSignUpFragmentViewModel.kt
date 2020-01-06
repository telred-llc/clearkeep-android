package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractSignUpFragmentViewModel : ViewModel() {

    abstract fun setDataForRegister(username: String, email: String, password: String)
    abstract fun getRegisterResult(): LiveData<Resource<String>>

    data class RegisterObject(val username: String, val email: String, val password: String)
}