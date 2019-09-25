package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractLoginFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class LoginFragmentViewModelFactory @Inject constructor(@Named(IFragment.LOGIN_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractLoginFragmentViewModel> {

    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractLoginFragmentViewModel::class.java);

    override fun getViewModel(): AbstractLoginFragmentViewModel {
        return viewModel;
    }
}