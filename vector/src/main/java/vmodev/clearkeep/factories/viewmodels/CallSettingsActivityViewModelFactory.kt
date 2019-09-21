package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.ICallSettingsActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.ICallSettingsActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractCallSettingActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class CallSettingsActivityViewModelFactory @Inject constructor(@Named(IActivity.CALL_SETTINGS_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : ICallSettingsActivityViewModelFactory {
    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractCallSettingActivityViewModel::class.java);
    override fun getViewModel(): AbstractCallSettingActivityViewModel {
        return viewModel;
    }
}