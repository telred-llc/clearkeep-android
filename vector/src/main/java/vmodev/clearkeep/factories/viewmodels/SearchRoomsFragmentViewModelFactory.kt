package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchRoomsFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class SearchRoomsFragmentViewModelFactory @Inject constructor(@Named(IFragment.SEARCH_ROOM_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractSearchRoomsFragmentViewModel> {
    private val viewModel = ViewModelProviders.of(fragment.getFragment(), factory).get(AbstractSearchRoomsFragmentViewModel::class.java);
    override fun getViewModel(): AbstractSearchRoomsFragmentViewModel {
        return viewModel;
    }
}