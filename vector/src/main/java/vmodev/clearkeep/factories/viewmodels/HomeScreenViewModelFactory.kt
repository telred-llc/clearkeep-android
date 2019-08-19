package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IHomeScreenActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IHomeScreenViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractHomeScreenActivityViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserViewModel
import javax.inject.Inject
import javax.inject.Named

class HomeScreenViewModelFactory @Inject constructor(@Named(IActivity.HOME_SCREEN_ACTIVITY) val activity: IActivity, val factory: ViewModelProvider.Factory) : IHomeScreenViewModelFactory {

    private var viewModel: AbstractHomeScreenActivityViewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractHomeScreenActivityViewModel::class.java);

    override fun getViewModel(): AbstractHomeScreenActivityViewModel {
        return viewModel;
    }
}