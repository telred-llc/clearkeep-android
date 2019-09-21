package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.KeyBackupRepository
import vmodev.clearkeep.repositories.SignatureRepository
import vmodev.clearkeep.viewmodelobjects.KeyBackup
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Signature
import vmodev.clearkeep.viewmodels.interfaces.AbstractBackupKeyManageFragmentViewModel
import javax.inject.Inject

class BackupKeyManageFragmentViewModel @Inject constructor(signatureRepository: SignatureRepository, keyBackupRepository: KeyBackupRepository) : AbstractBackupKeyManageFragmentViewModel() {

    private val _setIdForGetKeyBackup = MutableLiveData<String>();
    private val _setIdForDeleteKeyBackup = MutableLiveData<String>();

    private val _setIdForGetListSignature = MutableLiveData<String>();
    private val _getListSignatureResult = Transformations.switchMap(_setIdForGetListSignature) { input ->
        signatureRepository.getAllSignature(input);
    }
    private val _getKeyBackupResult = Transformations.switchMap(_setIdForGetKeyBackup) { input ->
        keyBackupRepository.getKeyBackup(input);
    }
    private val _getDeleteBackupKeyResult = Transformations.switchMap(_setIdForDeleteKeyBackup) { input -> keyBackupRepository.deleteBackupKey(input) }


    override fun getSignatureListResult(): LiveData<Resource<List<Signature>>> {
        return _getListSignatureResult;
    }

    override fun setIdForGetKeyBackup(id: String) {
        _setIdForGetKeyBackup.value = id;
    }

    override fun setIdForGetSignature(id: String) {
        _setIdForGetListSignature.value = id;
    }

    override fun getKeyBackupResult(): LiveData<Resource<KeyBackup>> {
        return _getKeyBackupResult;
    }

    override fun setIdForDeleteKeyBackup(id: String) {
        _setIdForDeleteKeyBackup.value = id;
    }

    override fun getDeleteKeyBackupResult(): LiveData<Resource<KeyBackup>> {
        return _getDeleteBackupKeyResult;
    }
}