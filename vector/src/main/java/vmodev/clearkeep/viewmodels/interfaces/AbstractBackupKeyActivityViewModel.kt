package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.KeyBackup
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractBackupKeyActivityViewModel : ViewModel() {
    abstract fun setIdForGetKeyBackup(id: String);
    abstract fun getKeyBackupResult(): LiveData<Resource<KeyBackup>>;

}