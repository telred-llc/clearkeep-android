package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractExportKeyActivityViewModel : ViewModel() {
    abstract fun getExportBackupKeyResult(): LiveData<Resource<String>>;
    abstract fun setExportBackupKey(passphare: String);
}