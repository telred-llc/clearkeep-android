package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.KeyBackupRepository
import vmodev.clearkeep.repositories.RoomRepository
import vmodev.clearkeep.repositories.UserRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.User
import vmodev.clearkeep.viewmodels.interfaces.AbstractEditProfileActivityViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractProfileActivityViewModel
import java.io.InputStream
import javax.inject.Inject

class ProfileActivityViewModel @Inject constructor(userRepository: UserRepository
                                                   , backupRepository: KeyBackupRepository
                                                   , roomRepository: RoomRepository) : AbstractProfileActivityViewModel() {

    private val _setIdForGetCurrentUser = MutableLiveData<String>();
    private val _setCheckNeedBackupWhenSignOut = MutableLiveData<Long>();
    private val _updateUser = MutableLiveData<UpdateUser>();
    private val _getGetCurrentUserResult = Transformations.switchMap(_setIdForGetCurrentUser) { input -> userRepository.loadUser(input) }
    private val _getNeedBackupWhenSignOutResult = Transformations.switchMap(_setCheckNeedBackupWhenSignOut) { input -> backupRepository.needBackupKeyWhenSignOut() }
    private val _getUpdateUserResult = Transformations.switchMap(_updateUser) { input -> userRepository.updateUser(input.userId, input.name, input.avatarImage) }

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

    override fun setUpdateUser(userId: String, name: String, avatarImage: InputStream?) {
        _updateUser.value = UpdateUser(userId, name, avatarImage);
    }

    override fun getUserUpdateResult(): LiveData<Resource<User>> {
        return _getUpdateUserResult
    }
}