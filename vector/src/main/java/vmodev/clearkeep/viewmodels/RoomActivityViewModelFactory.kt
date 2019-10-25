package vmodev.clearkeep.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IRoomActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class RoomActivityViewModelFactory @Inject constructor(@Named(IActivity.ROOM_ACTIVITY) val activity: IActivity, val factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractRoomActivityViewModel>  {
    private var viewModel: AbstractRoomActivityViewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractRoomActivityViewModel::class.java);
    override fun getViewModel(): AbstractRoomActivityViewModel {
        return viewModel;
    }
}