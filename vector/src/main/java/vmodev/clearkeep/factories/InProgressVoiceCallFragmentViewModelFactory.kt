package vmodev.clearkeep.factories

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractInProgressVoiceCallFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class InProgressVoiceCallFragmentViewModelFactory @Inject constructor(@Named(IFragment.IN_PROGRESS_VOICE_CALL_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractInProgressVoiceCallFragmentViewModel> {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractInProgressVoiceCallFragmentViewModel::class.java);
    override fun getViewModel(): AbstractInProgressVoiceCallFragmentViewModel {
        return viewModel;
    }
}