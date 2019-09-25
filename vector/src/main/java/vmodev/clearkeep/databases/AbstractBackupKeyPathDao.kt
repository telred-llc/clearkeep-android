package vmodev.clearkeep.databases

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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