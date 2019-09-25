package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.ISplashActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.ISplashActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractSplashActivityViewModel
import javax.inject.Inject

class SplashActivityViewModelFactory @Inject constructor(val activity: ISplashActivity, factory: ViewModelProvider.Factory) : ISplashActivityViewModelFactory {
    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractSplashActivityViewModel::class.java);
    override fun getViewModel(): AbstractSplashActivityViewModel {
        return viewModel;
    }
}