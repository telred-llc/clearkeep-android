package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomShareFileFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class RoomShareFileFragmentViewModelFactory @Inject constructor (@Named(IFragment.ROOM_SHARE_FILE_FRAGMENT) fragment : IFragment, factory : ViewModelProvider.Factory) : IViewModelFactory<AbstractRoomShareFileFragmentViewModel> {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractRoomShareFileFragmentViewModel::class.java);
    override fun getViewModel(): AbstractRoomShareFileFragmentViewModel {
        return viewModel;
    }
}