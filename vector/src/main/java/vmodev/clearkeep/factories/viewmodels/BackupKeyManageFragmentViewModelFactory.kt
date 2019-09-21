package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IBackupKeyManageFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractBackupKeyManageFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class BackupKeyManageFragmentViewModelFactory @Inject constructor(@Named(IFragment.BACKUP_KEY_MANAGE_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractBackupKeyManageFragmentViewModel> {

    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractBackupKeyManageFragmentViewModel::class.java);

    override fun getViewModel(): AbstractBackupKeyManageFragmentViewModel {
        return viewModel;
    }
}