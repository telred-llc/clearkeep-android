package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IExportKeyActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IExportKeyActivityViewModeFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractExportKeyActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class ExportKeyActivityViewModelFactory @Inject constructor(@Named(IActivity.EXPORT_KEY_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : IExportKeyActivityViewModeFactory {
    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractExportKeyActivityViewModel::class.java);
    override fun getViewModel(): AbstractExportKeyActivityViewModel {
        return viewModel;
    }
}