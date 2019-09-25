package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.matrix.androidsdk.crypto.data.ImportRoomKeysResult
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractTextFileRestoreBackupKeyFragmentViewModel : ViewModel() {
    abstract fun setRecoveryKey(key: String);
    abstract fun getRestoreResult(): LiveData<Resource<ImportRoomKeysResult>>
}