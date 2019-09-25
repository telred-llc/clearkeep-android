package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.BackupKeyPathRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractExportKeyActivityViewModel
import javax.inject.Inject

class ExportKeyActivityViewModel @Inject constructor(backupKeyPathRepository: BackupKeyPathRepository) : AbstractExportKeyActivityViewModel() {

    private val _passphrase = MutableLiveData<String>();
    private val _exportBackupKeyResult = Transformations.switchMap(_passphrase) { input ->
        backupKeyPathRepository.createNewBackupKey(input)
    }

    override fun getExportBackupKeyResult(): LiveData<Resource<String>> {
        return _exportBackupKeyResult;
    }

    override fun setExportBackupKey(passphare: String) {
        _passphrase.value = passphare;
    }
}