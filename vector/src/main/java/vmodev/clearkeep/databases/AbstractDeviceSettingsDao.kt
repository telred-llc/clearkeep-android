package vmodev.clearkeep.databases

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import vmodev.clearkeep.viewmodelobjects.DeviceSettings

@Dao
abstract class AbstractDeviceSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(deviceSettings: DeviceSettings)

    @Query("UPDATE deviceSettings SET notificationOnThisDevice =:status WHERE id =:id")
    abstract fun updateNotificationsOnThisDevice(id: String, status: Byte)

    @Query("UPDATE deviceSettings SET showDecryptedContent =:status WHERE id =:id")
    abstract fun updateShowDecryptedContent(id: String, status: Byte)

    @Query("UPDATE deviceSettings SET pinRoomWithMissedNotifications =:status WHERE id =:id")
    abstract fun updatePinRoomWithMissedNotifications(id: String, status: Byte)

    @Query("UPDATE deviceSettings SET pinRoomWithUnreadMessages =:status WHERE id =:id")
    abstract fun updatePinRoomWithUnreadMessages(id: String, status: Byte)

    @Query("UPDATE deviceSettings SET integratedCalling =:status WHERE id =:id")
    abstract fun updateIntegratedCalling(id: String, status: Byte)

    @Query("UPDATE deviceSettings SET sendAnonCrashAndUsageData =:status WHERE id =:id")
    abstract fun updateSendAnonCrashAndUsageData(id: String, status: Byte)

    @Query("UPDATE deviceSettings SET rageShakeToReportBug =:status WHERE id =:id")
    abstract fun updateRageShakeToReportBug(id: String, status: Byte)

    @Query("SELECT * FROM deviceSettings WHERE id =:id")
    abstract fun findById(id: String): LiveData<DeviceSettings>;

    @Query("UPDATE deviceSettings SET theme =:theme WHERE id =:id")
    abstract fun updateTheme(id: String, theme: Int): Int;

    @Query("DELETE FROM deviceSettings")
    abstract fun delete();

}