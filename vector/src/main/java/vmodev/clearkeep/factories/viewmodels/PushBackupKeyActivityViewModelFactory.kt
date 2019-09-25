package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractPushBackupKeyActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class PushBackupKeyActivityViewModelFactory @Inject constructor(@Named(IActivity.PUSH_BACKUP_KEY) activity: IActivity, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractPushBackupKeyActivityViewModel> {
    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractPushBackupKeyActivityViewModel::class.java);
    override fun getViewModel(): AbstractPushBackupKeyActivityViewModel {
        return viewModel;
    }
}