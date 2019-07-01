package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.IUserInformationActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IUserInformationActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserInformationActivityViewModel
import javax.inject.Inject

class UserInformationActivityViewModelFactory @Inject constructor(activity: IUserInformationActivity, factory: ViewModelProvider.Factory) : IUserInformationActivityViewModelFactory {

    private val viewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractUserInformationActivityViewModel::class.java);

    override fun getViewModel(): AbstractUserInformationActivityViewModel {
        return viewModel;
    }
}