package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractInviteUsersToRoomActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class InviteUsersToRoomViewModelFactory @Inject constructor(@Named(IFragment.INVITE_USERS_TO_ROOM_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractInviteUsersToRoomActivityViewModel> {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractInviteUsersToRoomActivityViewModel::class.java);

    override fun getViewModel(): AbstractInviteUsersToRoomActivityViewModel {
        return viewModel;
    }
}