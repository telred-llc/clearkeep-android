package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractForgotPasswordVerifyEmailFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class ForgotPasswordVerifyEmailFragmentViewModelFactory @Inject constructor(@Named(IFragment.FORGOT_PASSWORD_VERIFY_EMAIL_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractForgotPasswordVerifyEmailFragmentViewModel> {
    private val viewModel = ViewModelProviders.of(fragment.getFragment(), factory).get(AbstractForgotPasswordVerifyEmailFragmentViewModel::class.java);
    override fun getViewModel(): AbstractForgotPasswordVerifyEmailFragmentViewModel {
        return viewModel;
    }
}