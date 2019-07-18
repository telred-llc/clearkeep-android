package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractSignUpFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class SignUpFragmentViewModelFactory @Inject constructor(@Named(IFragment.SIGNUP_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractSignUpFragmentViewModel> {
    private val viewModel = ViewModelProviders.of(fragment.getFragment(), factory).get(AbstractSignUpFragmentViewModel::class.java);
    override fun getViewModel(): AbstractSignUpFragmentViewModel {
        return viewModel;
    }
}