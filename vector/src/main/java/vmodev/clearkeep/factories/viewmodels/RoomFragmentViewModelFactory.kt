package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.factories.viewmodels.interfaces.IRoomFragmentViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IRoomFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomFragmentViewModel
import javax.inject.Inject

class RoomFragmentViewModelFactory @Inject constructor(fragment: IRoomFragment, factory: ViewModelProvider.Factory) : IRoomFragmentViewModelFactory {
    private val viewModel = ViewModelProviders.of(fragment.getFragment(), factory).get(AbstractRoomFragmentViewModel::class.java);
    override fun getViewModel(): AbstractRoomFragmentViewModel {
        return viewModel;
    }
}