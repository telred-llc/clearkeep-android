package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.DeviceSettings
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractChangeThemeActivityViewModel : ViewModel() {
    abstract fun setDeviceSettingsId(devideId: String);
    abstract fun getDeviceSettingsResult(): LiveData<Resource<DeviceSettings>>;
    abstract fun setChangeTheme(userId: String, theme: Int);
    abstract fun getChangeThemeResult(): LiveData<Resource<DeviceSettings>>;

    data class ChangeTheme(val id: String, val theme: Int)
}