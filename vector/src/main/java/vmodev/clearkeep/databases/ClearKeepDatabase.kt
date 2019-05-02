package vmodev.clearkeep.databases

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

@Database(entities = [User::class, Room::class], version = 1, exportSchema = false)
abstract class ClearKeepDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao;
    abstract fun roomDao() : RoomDao;
}