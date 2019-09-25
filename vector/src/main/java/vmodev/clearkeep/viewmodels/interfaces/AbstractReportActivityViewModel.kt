package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.DeviceSettings
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractReportActivityViewModel : ViewModel() {
    abstract fun setSendAnonCrashAndUsageData(id: String, value: Byte);
    abstract fun setRageShakeToReportBug(id: String, value: Byte);
    abstract fun setIdForGetDeviceSettings(id: String);
    abstract fun getSendAnonCrashAndUsageDataResult(): LiveData<Resource<DeviceSettings>>;
    abstract fun getRageShakeToReportBug(): LiveData<Resource<DeviceSettings>>;
    abstract fun getDeviceSettingsResult(): LiveData<Resource<DeviceSettings>>;
    abstract fun getMergeGetDeiveSettingsResult(): LiveData<Resource<DeviceSettings>>;

    data class ChangeDataObject(val id: String, val value: Byte);
}