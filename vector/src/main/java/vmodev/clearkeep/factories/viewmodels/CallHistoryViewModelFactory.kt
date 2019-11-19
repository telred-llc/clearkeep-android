package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.ICallHistoryViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractCallHistoryViewModel
import javax.inject.Inject
import javax.inject.Named

class CallHistoryViewModelFactory @Inject constructor(@Named(IFragment.CALL_HISTORY_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : ICallHistoryViewModelFactory {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractCallHistoryViewModel::class.java)

    override fun getViewModel(): AbstractCallHistoryViewModel {
        return viewModel
    }

}