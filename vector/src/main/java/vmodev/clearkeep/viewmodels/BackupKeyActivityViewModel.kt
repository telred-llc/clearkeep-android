package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.KeyBackupRepository
import vmodev.clearkeep.viewmodelobjects.KeyBackup
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractBackupKeyActivityViewModel
import javax.inject.Inject

class BackupKeyActivityViewModel @Inject constructor(keyBackupRepository: KeyBackupRepository) : AbstractBackupKeyActivityViewModel() {
    private val _setIdForGetKeyBackup = MutableLiveData<String>();
    private val _getBackupKeyResult = Transformations.switchMap(_setIdForGetKeyBackup) { input -> keyBackupRepository.getKeyBackup(input) }
    override fun setIdForGetKeyBackup(id: String) {
        _setIdForGetKeyBackup.value = id;
    }

    override fun getKeyBackupResult(): LiveData<Resource<KeyBackup>> {
        return _getBackupKeyResult;
    }


}