package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.IExportKeyActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IExportKeyActivityViewModeFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractExportKeyActivityViewModel
import javax.inject.Inject

class ExportKeyActivityViewModelFactory @Inject constructor(activity: IExportKeyActivity, factory: ViewModelProvider.Factory) : IExportKeyActivityViewModeFactory {
    private val viewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractExportKeyActivityViewModel::class.java);
    override fun getViewModel(): AbstractExportKeyActivityViewModel {
        return viewModel;
    }
}