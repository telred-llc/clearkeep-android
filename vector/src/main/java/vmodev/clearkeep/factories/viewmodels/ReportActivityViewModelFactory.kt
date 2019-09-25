package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IReportActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IReportActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractReportActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class ReportActivityViewModelFactory @Inject constructor(@Named(IFragment.REPORT_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractReportActivityViewModel> {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractReportActivityViewModel::class.java);
    override fun getViewModel(): AbstractReportActivityViewModel {
        return viewModel;
    }
}