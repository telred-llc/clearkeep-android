package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import dagger.Module
import vmodev.clearkeep.viewmodelobjects.KeyBackup
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Signature

@Module
@Suppress("unused")
abstract class AbstractBackupKeyManageFragmentViewModel : ViewModel() {
    abstract fun setIdForGetKeyBackup(id: String);
    abstract fun getSignatureListResult(): LiveData<Resource<List<Signature>>>;
    abstract fun setIdForGetSignature(id: String);
    abstract fun getKeyBackupResult(): LiveData<Resource<KeyBackup>>;
    abstract fun setIdForDeleteKeyBackup(id: String);
    abstract fun getDeleteKeyBackupResult(): LiveData<Resource<KeyBackup>>;
}