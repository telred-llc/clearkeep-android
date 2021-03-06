package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IDeactivateAccountActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IDeactiavateAccountActivityViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractDeactivateAccountActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class DeactivateAccountActivityViewModelFactory @Inject constructor(@Named(IFragment.DEACTIVATE_ACCOUNT_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IDeactiavateAccountActivityViewModelFactory {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractDeactivateAccountActivityViewModel::class.java);
    override fun getViewModel(): AbstractDeactivateAccountActivityViewModel {
        return viewModel;
    }
}