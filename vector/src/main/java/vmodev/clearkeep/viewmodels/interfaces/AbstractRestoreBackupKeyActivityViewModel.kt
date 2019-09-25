package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractRestoreBackupKeyActivityViewModel : ViewModel() {
    abstract fun getAuthDataAsMegolmBackupAuthDataResult(): LiveData<Resource<String>>;
}