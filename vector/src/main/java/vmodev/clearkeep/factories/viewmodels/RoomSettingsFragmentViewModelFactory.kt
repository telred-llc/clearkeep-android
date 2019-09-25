package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomSettingsFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class RoomSettingsFragmentViewModelFactory @Inject constructor(@Named(IFragment.ROOM_SETTINGS_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractRoomSettingsFragmentViewModel> {

    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractRoomSettingsFragmentViewModel::class.java);

    override fun getViewModel(): AbstractRoomSettingsFragmentViewModel {
        return viewModel;
    }
}