package vmodev.clearkeep.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import vmodev.clearkeep.viewmodelobjects.KeyBackup
import vmodev.clearkeep.viewmodelobjects.Signature

@Dao
abstract class AbstractSignatureDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(item: Signature): Long;

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertList(items: List<Signature>): List<Long>;

    @Query("SELECT * FROM signature")
    abstract fun getAll(): LiveData<List<Signature>>;

    @Update
    abstract fun updateList(items: List<Signature>): Int

    @Query("SELECT signature.* FROM signature INNER JOIN keyBackup ON signature.key_backup_id = keyBackup.id WHERE keyBackup.id = :keyBackupId")
    abstract fun getListSignatureFromKeyBackup(keyBackupId: String): LiveData<List<Signature>>
}