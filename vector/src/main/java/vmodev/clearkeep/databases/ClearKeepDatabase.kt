package vmodev.clearkeep.databases

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import vmodev.clearkeep.viewmodelobjects.*

@Database(entities = [User::class, Room::class, RoomUserJoin::class, DeviceSettings::class, BackupKeyPath::class, Message::class, Signature::class
    , KeyBackup::class, LocalSettings::class], version = 6, exportSchema = false)
abstract class ClearKeepDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao;
    abstract fun roomDao(): RoomDao;
    abstract fun roomUserJoinDao(): AbstractRoomUserJoinDao;
    abstract fun deviceSettingsDao(): AbstractDeviceSettingsDao;
    abstract fun backupKeyPathDao(): AbstractBackupKeyPathDao;
    abstract fun messageDao(): AbstractMessageDao;
    abstract fun signatureDao(): AbstractSignatureDao;
    abstract fun keyBackupDao(): AbstractKeyBackupDao;
    abstract fun localSettingsDao() : AbstractLocalSettingsDao;
}