package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User

abstract class AbstractProfileActivityViewModel : ViewModel() {
    abstract fun setIdForGetCurrentUser(userId: String);
    abstract fun getCurrentUserResult(): LiveData<Resource<User>>
    abstract fun setCheckNeedBackupWhenSignOut(time: Long);
    abstract fun getNeedBackupWhenLogout(): LiveData<Resource<Boolean>>
}