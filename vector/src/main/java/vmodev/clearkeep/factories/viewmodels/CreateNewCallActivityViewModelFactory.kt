package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.ICreateNewCallActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.ICreateNewCallActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractCreateNewCallActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class CreateNewCallActivityViewModelFactory @Inject constructor(@Named(IActivity.CREATE_NEW_CALL_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : ICreateNewCallActivityViewModelFactory {

    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractCreateNewCallActivityViewModel::class.java);

    override fun getViewModel(): AbstractCreateNewCallActivityViewModel {
        return viewModel;
    }
}