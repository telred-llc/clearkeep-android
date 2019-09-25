package vmodev.clearkeep.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import vmodev.clearkeep.viewmodelobjects.*

@Database(entities = [User::class, Room::class, RoomUserJoin::class, DeviceSettings::class, BackupKeyPath::class, Message::class, Signature::class
    , KeyBackup::class, LocalSettings::class], version = 12, exportSchema = false)
abstract class ClearKeepDatabase : RoomDatabase() {
    abstract fun userDao(): AbstractUserDao;
    abstract fun roomDao(): AbstractRoomDao;
    abstract fun roomUserJoinDao(): AbstractRoomUserJoinDao;
    abstract fun deviceSettingsDao(): AbstractDeviceSettingsDao;
    abstract fun backupKeyPathDao(): AbstractBackupKeyPathDao;
    abstract fun messageDao(): AbstractMessageDao;
    abstract fun signatureDao(): AbstractSignatureDao;
    abstract fun keyBackupDao(): AbstractKeyBackupDao;
    abstract fun localSettingsDao() : AbstractLocalSettingsDao;
}