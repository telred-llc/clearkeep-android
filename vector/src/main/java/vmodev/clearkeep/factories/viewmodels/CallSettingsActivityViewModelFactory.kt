package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.ICallSettingsActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.ICallSettingsActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractCallSettingActivityViewModel
import javax.inject.Inject

class CallSettingsActivityViewModelFactory @Inject constructor(activity: ICallSettingsActivity, factory: ViewModelProvider.Factory) : ICallSettingsActivityViewModelFactory {
    private val viewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractCallSettingActivityViewModel::class.java);
    override fun getViewModel(): AbstractCallSettingActivityViewModel {
        return viewModel;
    }
}