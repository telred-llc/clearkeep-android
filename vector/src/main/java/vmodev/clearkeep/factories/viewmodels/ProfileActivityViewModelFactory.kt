package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractProfileActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class ProfileActivityViewModelFactory @Inject constructor(@Named(IActivity.PROFILE_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractProfileActivityViewModel> {
    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractProfileActivityViewModel::class.java);
    override fun getViewModel(): AbstractProfileActivityViewModel {
        return viewModel;
    }
}