package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.DeviceSettingsRepository
import vmodev.clearkeep.viewmodelobjects.DeviceSettings
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractDataBindingDaggerActivityViewModel
import javax.inject.Inject

class DataBindingDaggerActivityViewModel @Inject constructor(deviceSettingsRepository: DeviceSettingsRepository) : AbstractDataBindingDaggerActivityViewModel() {

    private val _deviceSettingsId = MutableLiveData<String>();
    private val _deviceSettingsResult = Transformations.switchMap(_deviceSettingsId) { input -> deviceSettingsRepository.loadDeviceSettings(input) }

    override fun setDeviceSettingsId(deviceSettingsId: String) {
        if (_deviceSettingsId.value != deviceSettingsId)
            _deviceSettingsId.value = deviceSettingsId;
    }

    override fun getDeviceSettingsResult(): LiveData<Resource<DeviceSettings>> {
        return _deviceSettingsResult;
    }
}