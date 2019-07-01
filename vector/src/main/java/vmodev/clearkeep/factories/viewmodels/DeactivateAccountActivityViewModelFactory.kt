package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.IDeactivateAccountActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IDeactiavateAccountActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractDeactivateAccountActivityViewModel
import javax.inject.Inject

class DeactivateAccountActivityViewModelFactory @Inject constructor(activity: IDeactivateAccountActivity, factory: ViewModelProvider.Factory) : IDeactiavateAccountActivityViewModelFactory {
    private val viewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractDeactivateAccountActivityViewModel::class.java);
    override fun getViewModel(): AbstractDeactivateAccountActivityViewModel {
        return viewModel;
    }
}