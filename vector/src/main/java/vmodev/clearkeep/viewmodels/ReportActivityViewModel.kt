package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.DeviceSettingsRepository
import vmodev.clearkeep.viewmodelobjects.DeviceSettings
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractReportActivityViewModel
import javax.inject.Inject

class ReportActivityViewModel @Inject constructor(deviceSettingsRepository: DeviceSettingsRepository) : AbstractReportActivityViewModel() {

    private val _mergeGetDeviceSettingsResult = MediatorLiveData<Resource<DeviceSettings>>();

    private val _setSendAnonCrashAndUsageData = MutableLiveData<ChangeDataObject>();
    private val _setRageShakeToReportBug = MutableLiveData<ChangeDataObject>();
    private val _setIdForGetDeviceSettings = MutableLiveData<String>();

    private val _getSendAnonCrashAndUsageDataResult = Transformations.switchMap(_setSendAnonCrashAndUsageData) { input -> deviceSettingsRepository.changeSendAnonCrashAndUsageData(input.id, input.value) }
    private val _getRageShakeToReportBug = Transformations.switchMap(_setRageShakeToReportBug) { input -> deviceSettingsRepository.changeRageShakeAndReportBug(input.id, input.value) }
    private val _getDeviceSettingsResult = Transformations.switchMap(_setIdForGetDeviceSettings) { input -> deviceSettingsRepository.loadDeviceSettings(input) }

    init {
        _mergeGetDeviceSettingsResult.addSource(_getDeviceSettingsResult) { t -> _mergeGetDeviceSettingsResult.value = t }
        _mergeGetDeviceSettingsResult.addSource(_getSendAnonCrashAndUsageDataResult) { t -> _mergeGetDeviceSettingsResult.value = t }
        _mergeGetDeviceSettingsResult.addSource(_getRageShakeToReportBug) { t -> _mergeGetDeviceSettingsResult.value = t }
    }

    override fun setSendAnonCrashAndUsageData(id: String, value: Byte) {
        _setSendAnonCrashAndUsageData.value = ChangeDataObject(id, value);
    }

    override fun setRageShakeToReportBug(id: String, value: Byte) {
        _setRageShakeToReportBug.value = ChangeDataObject(id, value);
    }

    override fun getSendAnonCrashAndUsageDataResult(): LiveData<Resource<DeviceSettings>> {
        return _getSendAnonCrashAndUsageDataResult;
    }

    override fun getRageShakeToReportBug(): LiveData<Resource<DeviceSettings>> {
        return _getRageShakeToReportBug;
    }

    override fun setIdForGetDeviceSettings(id: String) {
        _setIdForGetDeviceSettings.value = id;
    }

    override fun getDeviceSettingsResult(): LiveData<Resource<DeviceSettings>> {
        return _getDeviceSettingsResult;
    }

    override fun getMergeGetDeiveSettingsResult(): LiveData<Resource<DeviceSettings>> {
        return _mergeGetDeviceSettingsResult;
    }
}