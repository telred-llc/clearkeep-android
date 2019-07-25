package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.LocalSettingsRepository
import vmodev.clearkeep.viewmodelobjects.LocalSettings
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractProfileSettingsActivityViewModel
import javax.inject.Inject

class ProfileSettingsActivityViewModel @Inject constructor(private val localSettingsRepository: LocalSettingsRepository) : AbstractProfileSettingsActivityViewModel() {

    private val _setChangeTheme = MutableLiveData<Long>();
    private val _getChangeThemeResult = Transformations.switchMap(_setChangeTheme) { input -> localSettingsRepository.getLocalSettingsDao() }

    override fun setTimeToGetTheme(time: Long) {
        if (_setChangeTheme.value != time)
            _setChangeTheme.value = time;
    }

    override fun getThemeResult(): LiveData<Resource<LocalSettings>> {
        return _getChangeThemeResult;
    }

    override fun setChangeTheme(value: Int) {
        localSettingsRepository.changeTheme(value);
    }
}