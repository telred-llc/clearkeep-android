package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractRestoreBackupKeyActivityViewModel : ViewModel() {
    abstract fun getAuthDataAsMegolmBackupAuthDataResult(): LiveData<Resource<String>>;
}