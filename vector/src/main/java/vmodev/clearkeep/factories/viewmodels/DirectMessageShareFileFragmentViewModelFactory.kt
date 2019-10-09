package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.IDirectMessageFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IDirectMessageFragment
import vmodev.clearkeep.fragments.Interfaces.IDirectMessageShareFileFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractDirectMessageFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractDirectMessageShareFileFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class DirectMessageShareFileFragmentViewModelFactory @Inject constructor(@Named(IFragment.DIRECT_MESSAGE_SHARE_FILE_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractDirectMessageShareFileFragmentViewModel> {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractDirectMessageShareFileFragmentViewModel::class.java)
    override fun getViewModel(): AbstractDirectMessageShareFileFragmentViewModel {
        return viewModel;
    }
}