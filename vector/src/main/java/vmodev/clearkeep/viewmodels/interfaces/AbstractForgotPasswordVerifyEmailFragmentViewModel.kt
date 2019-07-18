package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import org.matrix.androidsdk.rest.model.pid.ThreePid
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractForgotPasswordVerifyEmailFragmentViewModel : ViewModel() {
    abstract fun setEmailForForgetPassword(email: String);
    abstract fun getForgetPasswordResult(): LiveData<Resource<ThreePid>>;
    abstract fun setPasswordForResetPassword(password: String, threePid: ThreePid);
    abstract fun getResetPasswordResult(): LiveData<Resource<String>>;

    data class ResetPasswordObject(val password: String, val threePid: ThreePid)
}