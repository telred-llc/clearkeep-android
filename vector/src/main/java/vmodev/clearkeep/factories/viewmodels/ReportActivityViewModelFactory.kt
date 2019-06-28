package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.IReportActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IReportActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractReportActivityViewModel
import javax.inject.Inject

class ReportActivityViewModelFactory @Inject constructor(activity: IReportActivity, factory: ViewModelProvider.Factory) : IReportActivityViewModelFactory {
    private val viewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractReportActivityViewModel::class.java);
    override fun getViewModel(): AbstractReportActivityViewModel {
        return viewModel;
    }
}