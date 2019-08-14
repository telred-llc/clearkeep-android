package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.DeviceSettings
import vmodev.clearkeep.viewmodelobjects.LocalSettings
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractDataBindingDaggerActivityViewModel : ViewModel() {
    abstract fun setTimeForGetTheme(time: Long);
    abstract fun getLocalSettingsResult(): LiveData<Resource<LocalSettings>>
}