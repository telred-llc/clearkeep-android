package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.IListRoomFragmentViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.Interfaces.IListRoomFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractListRoomFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class ListRoomFragmentViewModelFactory @Inject constructor(@Named(IFragment.LIST_ROOM_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IListRoomFragmentViewModelFactory {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractListRoomFragmentViewModel::class.java);
    override fun getViewModel(): AbstractListRoomFragmentViewModel {
        return viewModel;
    }
}