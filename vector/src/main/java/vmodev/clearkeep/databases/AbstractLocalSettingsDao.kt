package vmodev.clearkeep.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import vmodev.clearkeep.viewmodelobjects.LocalSettings

@Dao
abstract class AbstractLocalSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(item: LocalSettings): Long;

    @Query("SELECT * FROM localSettings LIMIT 1")
    abstract fun getItem(): LiveData<LocalSettings>

    @Query("UPDATE localsettings SET theme =:theme WHERE id =:id")
    abstract fun updateTheme(id: Int, theme: Int): Int;
}