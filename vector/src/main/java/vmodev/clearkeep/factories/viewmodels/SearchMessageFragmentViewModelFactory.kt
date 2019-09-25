package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchMessageFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class SearchMessageFragmentViewModelFactory @Inject constructor(@Named(IFragment.SEARCH_MESSAGE_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractSearchMessageFragmentViewModel> {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractSearchMessageFragmentViewModel::class.java);
    override fun getViewModel(): AbstractSearchMessageFragmentViewModel {
        return viewModel;
    }
}