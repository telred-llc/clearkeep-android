package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.LocalSettings
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractDataBindingDaggerActivityViewModel : ViewModel() {
    abstract fun setTimeForGetTheme(time: Long);
    abstract fun getLocalSettingsResult(): LiveData<Resource<LocalSettings>>
}