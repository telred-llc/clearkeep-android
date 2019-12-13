package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchActivityViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractUnifiedSearchActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class UnifiedSearchActivityViewModelFactory @Inject constructor(@Named(IActivity.UNIFIED_SEARCH_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractUnifiedSearchActivityViewModel> {
    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractUnifiedSearchActivityViewModel::class.java);
    override fun getViewModel(): AbstractUnifiedSearchActivityViewModel {
        return viewModel;
    }
}