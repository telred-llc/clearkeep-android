package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import vmodev.clearkeep.repositories.LoginRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractWaitingForVerifyEmailActivityViewModel
import javax.inject.Inject

class WaitingForVerifyEmailActivityViewModel @Inject constructor(private val loginRepository: LoginRepository) : AbstractWaitingForVerifyEmailActivityViewModel() {
    override fun getWaitingForVerifyEmailResult(): LiveData<Resource<String>> {
        return loginRepository.handleVerifyEmail();
    }
}