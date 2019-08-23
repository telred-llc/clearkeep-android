package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractCreateNewRoomActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class CreateNewRoomActivityViewModelFactory @Inject constructor(@Named(IActivity.CREATE_NEW_ROOM_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractCreateNewRoomActivityViewModel> {
    private val viewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractCreateNewRoomActivityViewModel::class.java);
    override fun getViewModel(): AbstractCreateNewRoomActivityViewModel {
        return viewModel;
    }
}