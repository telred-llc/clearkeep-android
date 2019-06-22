package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.DeviceSettings
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractDataBindingDaggerActivityViewModel : ViewModel() {
    abstract fun setDeviceSettingsId(deviceSettingsId: String);
    abstract fun getDeviceSettingsResult(): LiveData<Resource<DeviceSettings>>
}