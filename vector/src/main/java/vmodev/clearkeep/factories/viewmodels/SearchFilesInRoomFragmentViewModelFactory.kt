package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchFilesFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchFilesInRoomFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class SearchFilesInRoomFragmentViewModelFactory @Inject constructor(@Named(IFragment.SEARCH_FILES_IN_ROOM_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractSearchFilesInRoomFragmentViewModel> {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractSearchFilesInRoomFragmentViewModel::class.java);
    override fun getViewModel(): AbstractSearchFilesInRoomFragmentViewModel {
        return viewModel;
    }
}