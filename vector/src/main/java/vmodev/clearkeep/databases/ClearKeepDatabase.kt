package vmodev.clearkeep.databases

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import vmodev.clearkeep.viewmodelobjects.*

@Database(entities = [User::class, Room::class, RoomUserJoin::class, DeviceSettings::class, Message::class], version = 1, exportSchema = false)
abstract class ClearKeepDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao;
    abstract fun roomDao(): RoomDao;
    abstract fun roomUserJoinDao(): AbstractRoomUserJoinDao;
    abstract fun deviceSettingsDao(): AbstractDeviceSettingsDao;
    abstract fun messageDao(): AbstractMessageDao;
}