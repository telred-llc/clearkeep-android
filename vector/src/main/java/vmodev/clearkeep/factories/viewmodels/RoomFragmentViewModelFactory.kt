package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.IRoomFragmentViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.ISearchRoomFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class RoomFragmentViewModelFactory @Inject constructor(@Named(ISearchRoomFragment.SEARCH_ROOM_FRAGMENT) fragment: ISearchRoomFragment, factory: ViewModelProvider.Factory) : IRoomFragmentViewModelFactory {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractRoomFragmentViewModel::class.java);
    override fun getViewModel(): AbstractRoomFragmentViewModel {
        return viewModel;
    }
}