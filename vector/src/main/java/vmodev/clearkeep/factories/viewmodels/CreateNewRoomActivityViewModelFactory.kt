package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractCreateNewRoomActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class CreateNewRoomActivityViewModelFactory @Inject constructor(@Named(IFragment.CREATE_NEW_ROOM_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractCreateNewRoomActivityViewModel> {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractCreateNewRoomActivityViewModel::class.java);
    override fun getViewModel(): AbstractCreateNewRoomActivityViewModel {
        return viewModel;
    }
}