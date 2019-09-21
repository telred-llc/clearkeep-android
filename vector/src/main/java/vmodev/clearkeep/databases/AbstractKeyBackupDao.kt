package vmodev.clearkeep.databases

import androidx.lifecycle.LiveData
import androidx.room.*
import vmodev.clearkeep.viewmodelobjects.KeyBackup
import vmodev.clearkeep.viewmodelobjects.Signature

@Dao
abstract class AbstractKeyBackupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(item: KeyBackup): Long;

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertList(items: List<KeyBackup>): List<Long>;

    @Update
    abstract fun updateList(items: List<KeyBackup>): Int;

    @Update
    abstract fun update(item: KeyBackup): Int;

    @Query("SELECT * FROM keyBackup WHERE id = :id")
    abstract fun findById(id: String): LiveData<KeyBackup>

    @Query("SELECT signature.* FROM signature INNER JOIN keyBackup ON signature.key_backup_id = keyBackup.id WHERE keyBackup.id = :keyBackupId")
    abstract fun getListSignatureFromKeyBackup(keyBackupId: String): LiveData<List<Signature>>

    @Query("DELETE FROM keyBackup")
    abstract fun delete()
}