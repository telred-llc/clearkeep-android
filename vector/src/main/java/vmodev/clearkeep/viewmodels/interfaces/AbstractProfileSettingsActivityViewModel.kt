package vmodev.clearkeep.viewmodels.interfaces

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.LocalSettings
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractProfileSettingsActivityViewModel : ViewModel() {
    abstract fun setTimeToGetTheme(value : Long);
    abstract fun getThemeResult() : LiveData<Resource<LocalSettings>>
    abstract fun setChangeTheme(value : Int);
}