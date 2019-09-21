package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.INotificationSettingsActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.INotificationSettingsActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractNotificationSettingsActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class NotificationSettingsViewModelFactory @Inject constructor(@Named(IActivity.NOTIFICATION_SETTINGS_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : INotificationSettingsActivityViewModelFactory {

    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractNotificationSettingsActivityViewModel::class.java);

    override fun getViewModel(): AbstractNotificationSettingsActivityViewModel {
        return viewModel;
    }
}