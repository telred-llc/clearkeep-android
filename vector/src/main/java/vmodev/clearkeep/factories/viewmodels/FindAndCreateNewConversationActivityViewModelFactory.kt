package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractFindAndCreateNewConversationActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class FindAndCreateNewConversationActivityViewModelFactory @Inject constructor(@Named(IActivity.FIND_AND_CREATE_NEW_CONVERSATION_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory)
    : IViewModelFactory<AbstractFindAndCreateNewConversationActivityViewModel> {
    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractFindAndCreateNewConversationActivityViewModel::class.java);
    override fun getViewModel(): AbstractFindAndCreateNewConversationActivityViewModel {
        return viewModel;
    }
}