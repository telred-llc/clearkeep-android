package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import org.matrix.androidsdk.crypto.data.ImportRoomKeysResult
import vmodev.clearkeep.repositories.KeyBackupRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractPassphraseRestoreBackupKeyFragmentViewModel
import javax.inject.Inject

class PassphraseRestoreBackupKeyFragmentViewModel @Inject constructor(keyBackupRepository: KeyBackupRepository) : AbstractPassphraseRestoreBackupKeyFragmentViewModel() {
    private val _setPassphraseForRestore = MutableLiveData<String>();
    private val _getPassphraseRestoreResult = Transformations.switchMap(_setPassphraseForRestore) { input -> keyBackupRepository.restoreWithPassphrase(input) }
    override fun setPassphraseForRestore(passpharase: String) {
        _setPassphraseForRestore.value = passpharase;
    }

    override fun getPassphraseRestoreResult(): LiveData<Resource<ImportRoomKeysResult>> {
        return _getPassphraseRestoreResult;
    }
}