package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractHandlerVerifyEmailFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class HandlerVerifyEmailFragmentViewModelFactory @Inject constructor(@Named(IFragment.HANDLER_VERIFY_EMAIL_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractHandlerVerifyEmailFragmentViewModel> {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractHandlerVerifyEmailFragmentViewModel::class.java);
    override fun getViewModel(): AbstractHandlerVerifyEmailFragmentViewModel {
        return viewModel;
    }
}