package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.LocalSettings
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractProfileSettingsActivityViewModel : ViewModel() {
    abstract fun setTimeToGetTheme(value : Long);
    abstract fun getThemeResult() : LiveData<Resource<LocalSettings>>
    abstract fun setChangeTheme(value : Int);
}