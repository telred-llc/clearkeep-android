package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractProfileActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class ProfileActivityViewModelFactory @Inject constructor(@Named(IActivity.PROFILE_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractProfileActivityViewModel> {
    private val viewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractProfileActivityViewModel::class.java);
    override fun getViewModel(): AbstractProfileActivityViewModel {
        return viewModel;
    }
}