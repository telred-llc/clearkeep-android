package vmodev.clearkeep.viewmodels

import vmodev.clearkeep.repositories.LoginRepository
import vmodev.clearkeep.viewmodels.interfaces.AbstractForgotPasswordFragmentViewModel
import javax.inject.Inject

class ForgotPasswordFragmentViewModel @Inject constructor(loginRepository: LoginRepository) : AbstractForgotPasswordFragmentViewModel() {
}