package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractPushBackupKeyActivityViewModel : ViewModel() {
    abstract fun setPassphrase(passphrase: String);
    abstract fun getBackupKeyResult(): LiveData<Resource<String>>;
}