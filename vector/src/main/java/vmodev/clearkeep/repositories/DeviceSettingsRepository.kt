package vmodev.clearkeep.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import im.vector.R
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vmodev.clearkeep.databases.AbstractDeviceSettingsDao
import vmodev.clearkeep.viewmodelobjects.DeviceSettings
import vmodev.clearkeep.viewmodelobjects.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceSettingsRepository @Inject constructor(private val deviceSettingsDao: AbstractDeviceSettingsDao) {
    fun loadDeviceSettings(id: String): LiveData<Resource<DeviceSettings>> {
        return object : AbstractNetworkBoundSource<DeviceSettings, DeviceSettings>() {
            override fun saveCallResult(item: DeviceSettings) {
                deviceSettingsDao.insert(item);
            }

            override fun shouldFetch(data: DeviceSettings?): Boolean {
                return data == null;
            }

            override fun loadFromDb(): LiveData<DeviceSettings> {
                return deviceSettingsDao.findById(id);
            }

            override fun createCall(): LiveData<DeviceSettings> {
                return LiveDataReactiveStreams.fromPublisher(Observable.create<DeviceSettings> { emitter ->
                    val deviceSettings = DeviceSettings(id = id, integratedCalling = 1, notificationOnThisDevice = 1, pinRoomWithMissedNotifications = 1
                            , pinRoomWithUnreadMessages = 1, rageShakeToReportBug = 1, sendAnonCrashAndUsageData = 1, showDecryptedContent = 1, theme = R.style.LightTheme);
                    emitter.onNext(deviceSettings);
                    emitter.onComplete();
                }.observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData()
    }

    fun changeCallSettingsDeviceSettings(id: String, value: Byte): LiveData<Resource<DeviceSettings>> {
        return object : AbstractNetworkBoundSource<DeviceSettings, DeviceSettings>() {
            override fun saveCallResult(item: DeviceSettings) {
                deviceSettingsDao.updateIntegratedCalling(item.id, item.integratedCalling);
            }

            override fun shouldFetch(data: DeviceSettings?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<DeviceSettings> {
                return deviceSettingsDao.findById(id);
            }

            override fun createCall(): LiveData<DeviceSettings> {
                return LiveDataReactiveStreams.fromPublisher(Observable.create<DeviceSettings> { emitter ->
                    val deviceSettings = DeviceSettings(id = id, integratedCalling = value, showDecryptedContent = 1, sendAnonCrashAndUsageData = 1, rageShakeToReportBug = 1, pinRoomWithUnreadMessages = 1
                            , pinRoomWithMissedNotifications = 1, notificationOnThisDevice = 1, theme = R.style.LightTheme);
                    emitter.onNext(deviceSettings);
                    emitter.onComplete();
                }.observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }

    fun changeNotificationOnThisDeviceDeviceSettings(id: String, value: Byte): LiveData<Resource<DeviceSettings>> {
        return object : AbstractNetworkBoundSource<DeviceSettings, DeviceSettings>() {
            override fun saveCallResult(item: DeviceSettings) {
                deviceSettingsDao.updateNotificationsOnThisDevice(item.id, item.notificationOnThisDevice);
            }

            override fun shouldFetch(data: DeviceSettings?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<DeviceSettings> {
                return deviceSettingsDao.findById(id);
            }

            override fun createCall(): LiveData<DeviceSettings> {
                return LiveDataReactiveStreams.fromPublisher(Observable.create<DeviceSettings> { emitter ->
                    val deviceSettings = DeviceSettings(id = id, integratedCalling = 1, showDecryptedContent = 1, sendAnonCrashAndUsageData = 1, rageShakeToReportBug = 1, pinRoomWithUnreadMessages = 1
                            , pinRoomWithMissedNotifications = 1, notificationOnThisDevice = value, theme = R.style.LightTheme);
                    emitter.onNext(deviceSettings);
                    emitter.onComplete();
                }.observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }

    fun changeShowDecryptedContentDeviceSettings(id: String, value: Byte): LiveData<Resource<DeviceSettings>> {
        return object : AbstractNetworkBoundSource<DeviceSettings, DeviceSettings>() {
            override fun saveCallResult(item: DeviceSettings) {
                deviceSettingsDao.updateShowDecryptedContent(item.id, item.showDecryptedContent);
            }

            override fun shouldFetch(data: DeviceSettings?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<DeviceSettings> {
                return deviceSettingsDao.findById(id);
            }

            override fun createCall(): LiveData<DeviceSettings> {
                return LiveDataReactiveStreams.fromPublisher(Observable.create<DeviceSettings> { emitter ->
                    val deviceSettings = DeviceSettings(id = id, integratedCalling = 1, showDecryptedContent = value, sendAnonCrashAndUsageData = 1, rageShakeToReportBug = 1, pinRoomWithUnreadMessages = 1
                            , pinRoomWithMissedNotifications = 1, notificationOnThisDevice = 1, theme = R.style.LightTheme);
                    emitter.onNext(deviceSettings);
                    emitter.onComplete();
                }.observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }

    fun changePinRoomsMissedNotificationsDeviceSettings(id: String, value: Byte): LiveData<Resource<DeviceSettings>> {
        return object : AbstractNetworkBoundSource<DeviceSettings, DeviceSettings>() {
            override fun saveCallResult(item: DeviceSettings) {
                deviceSettingsDao.updatePinRoomWithMissedNotifications(item.id, item.pinRoomWithMissedNotifications);
            }

            override fun shouldFetch(data: DeviceSettings?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<DeviceSettings> {
                return deviceSettingsDao.findById(id);
            }

            override fun createCall(): LiveData<DeviceSettings> {
                return LiveDataReactiveStreams.fromPublisher(Observable.create<DeviceSettings> { emitter ->
                    val deviceSettings = DeviceSettings(id = id, integratedCalling = 1, showDecryptedContent = 1, sendAnonCrashAndUsageData = 1, rageShakeToReportBug = 1, pinRoomWithUnreadMessages = 1
                            , pinRoomWithMissedNotifications = value, notificationOnThisDevice = 1, theme = R.style.LightTheme);
                    emitter.onNext(deviceSettings);
                    emitter.onComplete();
                }.observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }

    fun changePinRoomsUnreadNotificationsDeviceSettings(id: String, value: Byte): LiveData<Resource<DeviceSettings>> {
        return object : AbstractNetworkBoundSource<DeviceSettings, DeviceSettings>() {
            override fun saveCallResult(item: DeviceSettings) {
                deviceSettingsDao.updatePinRoomWithUnreadMessages(item.id, item.pinRoomWithUnreadMessages);
            }

            override fun shouldFetch(data: DeviceSettings?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<DeviceSettings> {
                return deviceSettingsDao.findById(id);
            }

            override fun createCall(): LiveData<DeviceSettings> {
                return LiveDataReactiveStreams.fromPublisher(Observable.create<DeviceSettings> { emitter ->
                    val deviceSettings = DeviceSettings(id = id, integratedCalling = 1, showDecryptedContent = 1, sendAnonCrashAndUsageData = 1, rageShakeToReportBug = 1, pinRoomWithUnreadMessages = value
                            , pinRoomWithMissedNotifications = 1, notificationOnThisDevice = 1, theme = R.style.LightTheme);
                    emitter.onNext(deviceSettings);
                    emitter.onComplete();
                }.observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }

    fun changeTheme(id: String, theme: Int): LiveData<Resource<DeviceSettings>> {
        return object : AbstractNetworkBoundSource<DeviceSettings, DeviceSettings>() {
            override fun saveCallResult(item: DeviceSettings) {
                deviceSettingsDao.updateTheme(item.id, item.theme);
            }

            override fun shouldFetch(data: DeviceSettings?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<DeviceSettings> {
                return deviceSettingsDao.findById(id);
            }

            override fun createCall(): LiveData<DeviceSettings> {
                return LiveDataReactiveStreams.fromPublisher(Observable.create<DeviceSettings> { emitter ->
                    val deviceSettings = DeviceSettings(id = id, integratedCalling = 1, showDecryptedContent = 1, sendAnonCrashAndUsageData = 1, rageShakeToReportBug = 1, pinRoomWithUnreadMessages = 1
                            , pinRoomWithMissedNotifications = 1, notificationOnThisDevice = 1, theme = theme);
                    emitter.onNext(deviceSettings);
                    emitter.onComplete();
                }.observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).toFlowable(BackpressureStrategy.LATEST));
            }
        }.asLiveData();
    }
}