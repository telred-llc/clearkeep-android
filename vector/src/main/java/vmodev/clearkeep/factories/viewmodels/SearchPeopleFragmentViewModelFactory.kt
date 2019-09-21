package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchPeopleFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class SearchPeopleFragmentViewModelFactory @Inject constructor(@Named(IFragment.SEARCH_PEOPLE_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractSearchPeopleFragmentViewModel> {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractSearchPeopleFragmentViewModel::class.java);
    override fun getViewModel(): AbstractSearchPeopleFragmentViewModel {
        return viewModel;
    }
}