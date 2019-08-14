package vmodev.clearkeep.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import vmodev.clearkeep.viewmodelobjects.BackupKeyPath

@Dao
abstract class AbstractBackupKeyPathDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(item: BackupKeyPath): Long;

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun inserts(items: List<BackupKeyPath>): List<Long>;

    @Query("SELECT * FROM backupKeyPath")
    abstract fun getAllBackupKeyPath(): LiveData<BackupKeyPath>;

    @Query("DELETE FROM backupKeyPath")
    abstract fun delete();
}