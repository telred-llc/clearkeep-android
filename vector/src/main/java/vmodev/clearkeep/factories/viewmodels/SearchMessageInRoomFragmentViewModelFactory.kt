package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchMessageFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchMessageInroomFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class SearchMessageInRoomFragmentViewModelFactory @Inject constructor(@Named(IFragment.SEARCH_MESSAGE_IN_ROOM_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractSearchMessageInroomFragmentViewModel> {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractSearchMessageInroomFragmentViewModel::class.java);
    override fun getViewModel(): AbstractSearchMessageInroomFragmentViewModel {
        return viewModel;
    }
}