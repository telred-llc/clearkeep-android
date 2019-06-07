package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.IViewUserProfileActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewUserProfileActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractViewUserProfileActivityViewModel
import javax.inject.Inject

class ViewUserProfileActivityViewModelFactory @Inject constructor(activity: IViewUserProfileActivity, factory: ViewModelProvider.Factory) : IViewUserProfileActivityViewModelFactory {
    private val viewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractViewUserProfileActivityViewModel::class.java);
    override fun getViewModel(): AbstractViewUserProfileActivityViewModel {
        return viewModel;
    }
}