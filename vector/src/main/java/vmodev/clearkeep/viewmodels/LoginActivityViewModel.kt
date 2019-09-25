package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import vmodev.clearkeep.repositories.LoginRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractLoginActivityViewModel
import javax.inject.Inject

class LoginActivityViewModel @Inject constructor(private val loginRepository: LoginRepository) : AbstractLoginActivityViewModel() {
    override fun getRegistrationFlowsResponseResult(): LiveData<Resource<String>> {
        return loginRepository.setRegistrationFlowsResponse();
    }
}