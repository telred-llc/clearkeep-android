package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import vmodev.clearkeep.repositories.KeyBackupRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractRestoreBackupKeyActivityViewModel
import javax.inject.Inject

class RestoreBackupKeyActivityViewModel @Inject constructor(private val keyBackupRepository: KeyBackupRepository) : AbstractRestoreBackupKeyActivityViewModel() {
    override fun getAuthDataAsMegolmBackupAuthDataResult(): LiveData<Resource<String>> {
        return keyBackupRepository.getAuthDataAsMegolmBackupAuthData();
    }
}