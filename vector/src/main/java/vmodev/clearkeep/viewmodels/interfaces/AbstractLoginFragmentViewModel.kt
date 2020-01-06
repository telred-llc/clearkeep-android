package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractLoginFragmentViewModel : ViewModel() {

    abstract fun setUserNamePasswordForLogin(username: String, password: String)

    abstract fun getLoginResult(): LiveData<Resource<String>>

    data class LoginObject(val username: String, val password: String)
}