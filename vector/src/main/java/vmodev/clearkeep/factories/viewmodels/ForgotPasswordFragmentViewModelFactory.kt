package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractForgotPasswordFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class ForgotPasswordFragmentViewModelFactory @Inject constructor(@Named(IFragment.FORGOT_PASSWORD_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractForgotPasswordFragmentViewModel> {

    private val viewModel = ViewModelProviders.of(fragment.getFragment(), factory).get(AbstractForgotPasswordFragmentViewModel::class.java);

    override fun getViewModel(): AbstractForgotPasswordFragmentViewModel {
        return viewModel;
    }
}