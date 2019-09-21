package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import org.matrix.androidsdk.rest.model.pid.ThreePid
import vmodev.clearkeep.repositories.LoginRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractForgotPasswordVerifyEmailFragmentViewModel
import javax.inject.Inject

class ForgotPasswordVerifyEmailFragmentViewModel @Inject constructor(loginRepository: LoginRepository) : AbstractForgotPasswordVerifyEmailFragmentViewModel() {

    private val _setEmailForForgetPassword = MutableLiveData<String>();
    private val _setPasswordForResetPassword = MutableLiveData<ResetPasswordObject>();
    private val _getForgetPasswordResult = Transformations.switchMap(_setEmailForForgetPassword) { input -> loginRepository.forgetPassword(input) }
    private val _getResetPasswordResult = Transformations.switchMap(_setPasswordForResetPassword) { input -> loginRepository.resetPassword(input.password, input.threePid) }

    override fun setEmailForForgetPassword(email: String) {
        _setEmailForForgetPassword.value = email;
    }

    override fun getForgetPasswordResult(): LiveData<Resource<ThreePid>> {
        return _getForgetPasswordResult;
    }

    override fun setPasswordForResetPassword(password: String, threePid: ThreePid) {
        _setPasswordForResetPassword.value = ResetPasswordObject(password, threePid);
    }

    override fun getResetPasswordResult(): LiveData<Resource<String>> {
        return _getResetPasswordResult;
    }
}