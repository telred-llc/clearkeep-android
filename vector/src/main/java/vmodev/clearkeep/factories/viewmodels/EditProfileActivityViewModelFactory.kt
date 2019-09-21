package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractEditProfileActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class EditProfileActivityViewModelFactory @Inject constructor(@Named(IActivity.EDIT_PROFILE_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractEditProfileActivityViewModel> {
    private val viewModel: AbstractEditProfileActivityViewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractEditProfileActivityViewModel::class.java)
    override fun getViewModel(): AbstractEditProfileActivityViewModel {
        return viewModel;
    }
}