package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractRestoreBackupKeyActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class RestoreBackupKeyActivityViewModelFactory @Inject constructor(@Named(IActivity.RESTORE_BACKUP_KEY) activity: IActivity, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractRestoreBackupKeyActivityViewModel> {
    private val viewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractRestoreBackupKeyActivityViewModel::class.java);
    override fun getViewModel(): AbstractRestoreBackupKeyActivityViewModel {
        return viewModel;
    }
}