package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractPushBackupKeyActivityViewModel : ViewModel() {
    abstract fun setPassphrase(passphrase: String);
    abstract fun getBackupKeyResult(): LiveData<Resource<String>>;
}