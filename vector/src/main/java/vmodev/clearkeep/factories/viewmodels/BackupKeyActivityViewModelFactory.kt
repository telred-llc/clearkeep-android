package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractBackupKeyActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class BackupKeyActivityViewModelFactory @Inject constructor(@Named(IActivity.BACKUP_KEY) activity: IActivity, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractBackupKeyActivityViewModel> {
    private val viewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractBackupKeyActivityViewModel::class.java);
    override fun getViewModel(): AbstractBackupKeyActivityViewModel {
        return viewModel;
    }
}