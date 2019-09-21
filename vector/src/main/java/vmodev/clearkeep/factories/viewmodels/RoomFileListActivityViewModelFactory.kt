package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IRoomFileListActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IRoomFileListActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomFileListActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class RoomFileListActivityViewModelFactory @Inject constructor(@Named(IActivity.ROOM_FILES_LIST_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : IRoomFileListActivityViewModelFactory {

    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractRoomFileListActivityViewModel::class.java);

    override fun getViewModel(): AbstractRoomFileListActivityViewModel {
        return viewModel;
    }
}