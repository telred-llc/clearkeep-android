package vmodev.clearkeep.viewmodels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.DeviceSettingsRepository
import vmodev.clearkeep.viewmodelobjects.DeviceSettings
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractCallSettingActivityViewModel
import javax.inject.Inject

class CallSettingsActivityViewModel @Inject constructor(deviceSettingsRepository: DeviceSettingsRepository) : AbstractCallSettingActivityViewModel() {
    private val _idForDeviceSettings = MediatorLiveData<String>();
    private val _getDeviceSettingsResult = Transformations.switchMap(_idForDeviceSettings) { input -> deviceSettingsRepository.loadDeviceSettings(input) }
    private val _changeDeviceSettingsValue = MediatorLiveData<ChangeDeviceSettingsObject>();
    private val _getChangeDeviceSettingValue = Transformations.switchMap(_changeDeviceSettingsValue) { input -> deviceSettingsRepository.changeCallSettingsDeviceSettings(input.id, input.value) }

    override fun getDeviceSettingsResult(): LiveData<Resource<DeviceSettings>> {
        return _getDeviceSettingsResult;
    }

    override fun setIdForDeviceSettingsResult(id: String) {
        if (_idForDeviceSettings.value != id)
            _idForDeviceSettings.value = id;
    }

    override fun getChangeDeviceSettingsResult(): LiveData<Resource<DeviceSettings>> {
        return _getChangeDeviceSettingValue;
    }

    override fun setChangeDeviceSettingValue(id: String, value: Byte) {
        _changeDeviceSettingsValue.value = ChangeDeviceSettingsObject(id = id, value = value);
    }
}