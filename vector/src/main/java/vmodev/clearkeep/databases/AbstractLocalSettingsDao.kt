package vmodev.clearkeep.databases

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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