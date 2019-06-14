package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractExportKeyActivityViewModel : ViewModel() {
    abstract fun getExportBackupKeyResult(): LiveData<Resource<String>>;
    abstract fun setExportBackupKey(passphare: String);
}