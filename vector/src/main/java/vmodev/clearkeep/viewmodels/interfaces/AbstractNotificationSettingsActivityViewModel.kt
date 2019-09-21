package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.DeviceSettings
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractNotificationSettingsActivityViewModel : ViewModel() {
    abstract fun getDeviceSettingsMergeResult(): LiveData<Resource<DeviceSettings>>;
    abstract fun setFindDeviceSettings(id: String);
    abstract fun setChangeNotificationOnThisDevice(id: String, value: Byte)
    abstract fun setChangeShowDecryptedContent(id: String, value: Byte)
    abstract fun setChangePinRoomsMissedNotifications(id: String, value: Byte)
    abstract fun setChangePinRoomsUnreadNotifications(id: String, value: Byte)

    data class ChangeDeviceSettingsObject(val id: String, val value: Byte);
}