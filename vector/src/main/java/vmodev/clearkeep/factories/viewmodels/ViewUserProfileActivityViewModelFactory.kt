package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IViewUserProfileActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewUserProfileActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractViewUserProfileActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class ViewUserProfileActivityViewModelFactory @Inject constructor(@Named(IActivity.VIEW_USER_PROFILE_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : IViewUserProfileActivityViewModelFactory {
    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractViewUserProfileActivityViewModel::class.java);
    override fun getViewModel(): AbstractViewUserProfileActivityViewModel {
        return viewModel;
    }
}