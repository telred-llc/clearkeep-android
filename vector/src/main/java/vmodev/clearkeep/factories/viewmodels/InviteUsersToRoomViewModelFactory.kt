package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractInviteUsersToRoomActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class InviteUsersToRoomViewModelFactory @Inject constructor(@Named(IActivity.INVITE_USERS_TO_ROOM_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractInviteUsersToRoomActivityViewModel> {
    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractInviteUsersToRoomActivityViewModel::class.java);

    override fun getViewModel(): AbstractInviteUsersToRoomActivityViewModel {
        return viewModel;
    }
}