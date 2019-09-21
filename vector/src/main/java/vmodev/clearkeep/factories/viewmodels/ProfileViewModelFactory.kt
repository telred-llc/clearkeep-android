package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IProfileActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IProfileViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import javax.inject.Inject

class ProfileViewModelFactory @Inject constructor(val activity: IProfileActivity, val factory: ViewModelProvider.Factory) : IProfileViewModelFactory {
    override fun getViewModel(): AbstractRoomViewModel {
        return ViewModelProvider(activity.getActivity(), factory).get(AbstractRoomViewModel::class.java);
    }
}