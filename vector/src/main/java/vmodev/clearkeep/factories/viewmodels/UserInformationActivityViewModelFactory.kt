package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IUserInformationActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IUserInformationActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserInformationActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class UserInformationActivityViewModelFactory @Inject constructor(@Named(IActivity.USER_INFORMATION_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : IUserInformationActivityViewModelFactory {

    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractUserInformationActivityViewModel::class.java);

    override fun getViewModel(): AbstractUserInformationActivityViewModel {
        return viewModel;
    }
}