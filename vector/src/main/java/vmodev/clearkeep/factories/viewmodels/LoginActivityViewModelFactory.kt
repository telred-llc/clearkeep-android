package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractLoginActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class LoginActivityViewModelFactory @Inject constructor(@Named(IActivity.LOGIN_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractLoginActivityViewModel> {
    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractLoginActivityViewModel::class.java);
    override fun getViewModel(): AbstractLoginActivityViewModel {
        return viewModel;
    }
}