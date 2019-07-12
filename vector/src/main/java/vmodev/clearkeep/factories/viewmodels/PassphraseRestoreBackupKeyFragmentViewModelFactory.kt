package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractPassphraseRestoreBackupKeyFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class PassphraseRestoreBackupKeyFragmentViewModelFactory @Inject constructor(@Named(IFragment.PASSPHRASE_RESTORE_BACK_UP_KEY_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractPassphraseRestoreBackupKeyFragmentViewModel> {
    private val viewModel = ViewModelProviders.of(fragment.getFragment(), factory).get(AbstractPassphraseRestoreBackupKeyFragmentViewModel::class.java);
    override fun getViewModel(): AbstractPassphraseRestoreBackupKeyFragmentViewModel {
        return viewModel;
    }
}