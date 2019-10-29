package vmodev.clearkeep.factories

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.IncomingCallFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractIncomingCallFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class IncomingCallFragmentViewModelFactory @Inject constructor(@Named(IFragment.INCOMING_CALL_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractIncomingCallFragmentViewModel> {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractIncomingCallFragmentViewModel::class.java)
    override fun getViewModel(): AbstractIncomingCallFragmentViewModel {
        return viewModel;
    }
}