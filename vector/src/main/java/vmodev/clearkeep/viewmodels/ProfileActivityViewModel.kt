package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.KeyBackupRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractProfileActivityViewModel
import javax.inject.Inject

class ProfileActivityViewModel @Inject constructor(userRepository: UserRepository, backupRepository: KeyBackupRepository) : AbstractProfileActivityViewModel() {

    private val _setIdForGetCurrentUser = MutableLiveData<String>();
    private val _setCheckNeedBackupWhenSignOut = MutableLiveData<Long>();
    private val _getGetCurrentUserResult = Transformations.switchMap(_setIdForGetCurrentUser) { input -> userRepository.loadUser(input) }
    private val _getNeedBackupWhenSignOutResult = Transformations.switchMap(_setCheckNeedBackupWhenSignOut) { input -> backupRepository.needBackupKeyWhenSignOut() }

    override fun setIdForGetCurrentUser(userId: String) {
        if (_setIdForGetCurrentUser.value != userId)
            _setIdForGetCurrentUser.value = userId;
    }

    override fun getCurrentUserResult(): LiveData<Resource<User>> {
        return _getGetCurrentUserResult;
    }

    override fun getNeedBackupWhenLogout(): LiveData<Resource<Int>> {
        return _getNeedBackupWhenSignOutResult;
    }

    override fun setCheckNeedBackupWhenSignOut(time: Long) {
        _setCheckNeedBackupWhenSignOut.value = time;
    }
}