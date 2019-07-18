package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractWaitingForVerifyEmailActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class WaitingForVerifyEmailActivityViewModelFactory @Inject constructor(@Named(IActivity.WAITING_FOR_VERIFY_EMAIL_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractWaitingForVerifyEmailActivityViewModel> {

    private val viewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractWaitingForVerifyEmailActivityViewModel::class.java);

    override fun getViewModel(): AbstractWaitingForVerifyEmailActivityViewModel {
        return viewModel;
    }
}