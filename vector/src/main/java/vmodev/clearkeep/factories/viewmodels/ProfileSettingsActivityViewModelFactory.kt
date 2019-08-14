package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IProfileSettingsActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IProfileSettingsActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractProfileSettingsActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class ProfileSettingsActivityViewModelFactory @Inject constructor(@Named(IActivity.PROFILE_SETTINGS_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : IProfileSettingsActivityViewModelFactory {
    private val viewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractProfileSettingsActivityViewModel::class.java)
    override fun getViewModel(): AbstractProfileSettingsActivityViewModel {
        return viewModel;
    }
}