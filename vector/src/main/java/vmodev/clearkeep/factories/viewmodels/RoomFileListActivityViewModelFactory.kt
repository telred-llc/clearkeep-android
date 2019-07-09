package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.IRoomFileListActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IRoomFileListActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomFileListActivityViewModel
import javax.inject.Inject

class RoomFileListActivityViewModelFactory @Inject constructor(activity: IRoomFileListActivity, factory: ViewModelProvider.Factory) : IRoomFileListActivityViewModelFactory {

    private val viewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractRoomFileListActivityViewModel::class.java);

    override fun getViewModel(): AbstractRoomFileListActivityViewModel {
        return viewModel;
    }
}