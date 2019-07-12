package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.KeyBackupRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractPushBackupKeyActivityViewModel
import javax.inject.Inject

class PushBackupKeyViewModel @Inject constructor(keyBackupRepository: KeyBackupRepository) : AbstractPushBackupKeyActivityViewModel() {

    private val _setPassphrase = MutableLiveData<String>();
    private val _backupKeyResult = Transformations.switchMap(_setPassphrase) { input -> keyBackupRepository.startBackupKey(input) }

    override fun setPassphrase(passphrase: String) {
        _setPassphrase.value = passphrase;
    }

    override fun getBackupKeyResult(): LiveData<Resource<String>> {
        return _backupKeyResult;
    }
}