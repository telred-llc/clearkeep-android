package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.INotificationSettingsActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.INotificationSettingsActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractNotificationSettingsActivityViewModel
import javax.inject.Inject

class NotificationSettingsViewModelFactory @Inject constructor(activity: INotificationSettingsActivity, factory: ViewModelProvider.Factory) : INotificationSettingsActivityViewModelFactory {

    private val viewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractNotificationSettingsActivityViewModel::class.java);

    override fun getViewModel(): AbstractNotificationSettingsActivityViewModel {
        return viewModel;
    }
}