package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.ICreateNewCallActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.ICreateNewCallActivityViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractCreateNewCallActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class CreateNewCallActivityViewModelFactory @Inject constructor(@Named(IFragment.CREATE_NEW_CALL_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : ICreateNewCallActivityViewModelFactory {

    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractCreateNewCallActivityViewModel::class.java);

    override fun getViewModel(): AbstractCreateNewCallActivityViewModel {
        return viewModel;
    }
}