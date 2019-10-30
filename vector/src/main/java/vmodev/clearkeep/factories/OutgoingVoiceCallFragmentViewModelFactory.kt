package vmodev.clearkeep.factories

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractOutgoingVoiceCallFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class OutgoingVoiceCallFragmentViewModelFactory @Inject constructor(@Named(IFragment.OUTGOING_VOICE_CALL_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractOutgoingVoiceCallFragmentViewModel> {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractOutgoingVoiceCallFragmentViewModel::class.java);
    override fun getViewModel(): AbstractOutgoingVoiceCallFragmentViewModel {
        return viewModel;
    }
}