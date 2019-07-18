package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import vmodev.clearkeep.repositories.LoginRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractHandlerVerifyEmailFragmentViewModel
import javax.inject.Inject

class HandlerVerifyEmailFragmentViewModel @Inject constructor(private val loginRepository: LoginRepository) : AbstractHandlerVerifyEmailFragmentViewModel() {
    override fun getRegistrationFlowsResponseResult(): LiveData<Resource<String>> {
        return loginRepository.handleVerifyEmail();
    }
}