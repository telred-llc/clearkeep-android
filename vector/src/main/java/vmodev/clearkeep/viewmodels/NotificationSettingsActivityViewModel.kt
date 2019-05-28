package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.DeviceSettingsRepository
import vmodev.clearkeep.viewmodelobjects.DeviceSettings
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractNotificationSettingsActivityViewModel
import javax.inject.Inject

class NotificationSettingsActivityViewModel @Inject constructor(deviceSettingsRepository: DeviceSettingsRepository) : AbstractNotificationSettingsActivityViewModel() {

    private val _mergeGetDeviceSettingsResult = MediatorLiveData<Resource<DeviceSettings>>();

    private val _findDeviceSettings = MutableLiveData<String>();
    private val _changeNotificationOnThisDevice = MutableLiveData<ChangeDeviceSettingsObject>();
    private val _changeShowDecryptedContent = MutableLiveData<ChangeDeviceSettingsObject>();
    private val _changePinRoomsMissedNotifications = MutableLiveData<ChangeDeviceSettingsObject>();
    private val _changePinRoomsUnreadNotifications = MutableLiveData<ChangeDeviceSettingsObject>();

    private val _findDeviceSettingsResult : LiveData<Resource<DeviceSettings>> = Transformations.switchMap(_findDeviceSettings) { input -> deviceSettingsRepository.loadDeviceSettings(input) }
    private val _changeNotificationOnThisDeviceResult : LiveData<Resource<DeviceSettings>> = Transformations.switchMap(_changeNotificationOnThisDevice) { input -> deviceSettingsRepository.changeNotificationOnThisDeviceDeviceSettings(input.id, input.value) }
    private val _changeShowDecryptedContentResult : LiveData<Resource<DeviceSettings>> = Transformations.switchMap(_changeShowDecryptedContent) { input -> deviceSettingsRepository.changeShowDecryptedContentDeviceSettings(input.id, input.value) }
    private val _changePinRoomsMissedNotificationsResult : LiveData<Resource<DeviceSettings>> = Transformations.switchMap(_changePinRoomsMissedNotifications) { input -> deviceSettingsRepository.changePinRoomsMissedNotificationsDeviceSettings(input.id, input.value) }
    private val _changePinRoomsUnreadNotificationsResult : LiveData<Resource<DeviceSettings>> = Transformations.switchMap(_changePinRoomsUnreadNotifications) { input -> deviceSettingsRepository.changePinRoomsUnreadNotificationsDeviceSettings(input.id, input.value) }

    init {
        _mergeGetDeviceSettingsResult.addSource(_findDeviceSettingsResult) { t -> _mergeGetDeviceSettingsResult.value = t };
        _mergeGetDeviceSettingsResult.addSource(_changeNotificationOnThisDeviceResult) { t -> _mergeGetDeviceSettingsResult.value = t }
        _mergeGetDeviceSettingsResult.addSource(_changeShowDecryptedContentResult) { t -> _mergeGetDeviceSettingsResult.value = t }
        _mergeGetDeviceSettingsResult.addSource(_changePinRoomsMissedNotificationsResult) { t -> _mergeGetDeviceSettingsResult.value = t }
        _mergeGetDeviceSettingsResult.addSource(_changePinRoomsUnreadNotificationsResult) { t -> _mergeGetDeviceSettingsResult.value = t }
    }

    override fun getDeviceSettingsMergeResult(): LiveData<Resource<DeviceSettings>> {
        return _mergeGetDeviceSettingsResult;
    }

    override fun setFindDeviceSettings(id: String) {
        _findDeviceSettings.value = id;
    }

    override fun setChangeNotificationOnThisDevice(id: String, value: Byte) {
        _changeNotificationOnThisDevice.value = ChangeDeviceSettingsObject(id, value);
    }

    override fun setChangeShowDecryptedContent(id: String, value: Byte) {
        _changeShowDecryptedContent.value = ChangeDeviceSettingsObject(id, value);
    }

    override fun setChangePinRoomsMissedNotifications(id: String, value: Byte) {
        _changePinRoomsMissedNotifications.value = ChangeDeviceSettingsObject(id, value);
    }

    override fun setChangePinRoomsUnreadNotifications(id: String, value: Byte) {
        _changePinRoomsUnreadNotifications.value = ChangeDeviceSettingsObject(id, value);
    }
}