package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.util.Log
import vmodev.clearkeep.di.AbstractExportKeyActivityModule
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