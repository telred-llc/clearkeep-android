package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import org.matrix.androidsdk.crypto.data.ImportRoomKeysResult
import vmodev.clearkeep.repositories.KeyBackupRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractTextFileRestoreBackupKeyFragmentViewModel
import javax.inject.Inject

class TextFileRestoreBackupKeyFragmentViewModel @Inject constructor(keyBackupRepository: KeyBackupRepository) : AbstractTextFileRestoreBackupKeyFragmentViewModel() {
    private val _setRecoveryKey = MutableLiveData<String>();
    private val _getRestoreResult = Transformations.switchMap(_setRecoveryKey) { input -> keyBackupRepository.restoreWithRecoveryKey(input) }
    override fun setRecoveryKey(key: String) {
        _setRecoveryKey.value = key;
    }

    override fun getRestoreResult(): LiveData<Resource<ImportRoomKeysResult>> {
        return _getRestoreResult;
    }
}