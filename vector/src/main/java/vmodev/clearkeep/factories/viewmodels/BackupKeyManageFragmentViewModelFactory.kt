package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IBackupKeyManageFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractBackupKeyManageFragmentViewModel
import javax.inject.Inject

class BackupKeyManageFragmentViewModelFactory @Inject constructor(fragment: IBackupKeyManageFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractBackupKeyManageFragmentViewModel> {

    private val viewModel = ViewModelProviders.of(fragment.getFragment(), factory).get(AbstractBackupKeyManageFragmentViewModel::class.java);

    override fun getViewModel(): AbstractBackupKeyManageFragmentViewModel {
        return viewModel;
    }
}