package vmodev.clearkeep.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import vmodev.clearkeep.repositories.LocalSettingsRepository
import vmodev.clearkeep.viewmodelobjects.LocalSettings
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractDataBindingDaggerActivityViewModel
import javax.inject.Inject

class DataBindingDaggerActivityViewModel @Inject constructor(localSettingsRepository: LocalSettingsRepository) : AbstractDataBindingDaggerActivityViewModel() {

    private val _setTimeForGetChangeTheme = MutableLiveData<Long>();
    private val _getChangeThemeResult = Transformations.switchMap(_setTimeForGetChangeTheme) { input -> localSettingsRepository.getLocalSettingsDao() }

    override fun setTimeForGetTheme(time: Long) {
        if (_setTimeForGetChangeTheme.value != time)
            _setTimeForGetChangeTheme.value = time;
    }

    override fun getLocalSettingsResult(): LiveData<Resource<LocalSettings>> {
        return _getChangeThemeResult;
    }
}