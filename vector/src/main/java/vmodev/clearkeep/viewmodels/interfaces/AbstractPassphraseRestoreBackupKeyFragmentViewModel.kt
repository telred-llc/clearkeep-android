package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import org.matrix.androidsdk.crypto.data.ImportRoomKeysResult
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractPassphraseRestoreBackupKeyFragmentViewModel : ViewModel() {
    abstract fun setPassphraseForRestore(passpharase: String);
    abstract fun getPassphraseRestoreResult(): LiveData<Resource<ImportRoomKeysResult>>;
}