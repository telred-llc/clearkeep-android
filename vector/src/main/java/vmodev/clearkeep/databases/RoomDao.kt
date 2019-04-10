package vmodev.clearkeep.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
abstract class RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg rooms : Room);
    @Query("SELECT * FROM room WHERE type = :type")
    abstract fun loadWithType(type : Int) : LiveData<List<vmodev.clearkeep.viewmodelobjects.Room>>;
}