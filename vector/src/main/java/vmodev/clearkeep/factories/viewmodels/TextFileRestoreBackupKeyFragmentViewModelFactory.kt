package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractTextFileRestoreBackupKeyFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class TextFileRestoreBackupKeyFragmentViewModelFactory @Inject constructor(@Named(IFragment.TEXT_FILE_RESTORE_BACK_UP_KEY_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractTextFileRestoreBackupKeyFragmentViewModel> {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractTextFileRestoreBackupKeyFragmentViewModel::class.java);
    override fun getViewModel(): AbstractTextFileRestoreBackupKeyFragmentViewModel {
        return viewModel;
    }
}