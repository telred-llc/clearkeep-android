package vmodev.clearkeep.factories.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractFindAndCreateNewConversationActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class FindAndCreateNewConversationActivityViewModelFactory @Inject constructor(@Named(IFragment.FIND_AND_CREATE_NEW_CONVERSATION_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory)
    : IViewModelFactory<AbstractFindAndCreateNewConversationActivityViewModel> {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractFindAndCreateNewConversationActivityViewModel::class.java);
    override fun getViewModel(): AbstractFindAndCreateNewConversationActivityViewModel {
        return viewModel;
    }
}