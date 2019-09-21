package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.DeviceSettingsRepository
import vmodev.clearkeep.viewmodelobjects.DeviceSettings
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractChangeThemeActivityViewModel
import javax.inject.Inject

class ChangeThemeActivityViewModel @Inject constructor(deviceSettingsRepository: DeviceSettingsRepository) : AbstractChangeThemeActivityViewModel() {

    private val _deviceSettingsId = MutableLiveData<String>();
    private val _theme = MutableLiveData<ChangeTheme>();
    private val _getDeviceSettingsResult = Transformations.switchMap(_deviceSettingsId) { input -> deviceSettingsRepository.loadDeviceSettings(input) }
    private val _getChangeThemeResult = Transformations.switchMap(_theme) { input -> deviceSettingsRepository.changeTheme(input.id, input.theme) }

    override fun setDeviceSettingsId(devideId: String) {
        if (_deviceSettingsId.value != devideId)
            _deviceSettingsId.value = devideId;
    }

    override fun getDeviceSettingsResult(): LiveData<Resource<DeviceSettings>> {
        return _getDeviceSettingsResult;
    }

    override fun setChangeTheme(userId: String, theme: Int) {
        _theme.value = ChangeTheme(userId, theme);
    }

    override fun getChangeThemeResult(): LiveData<Resource<DeviceSettings>> {
        return _getChangeThemeResult;
    }
}