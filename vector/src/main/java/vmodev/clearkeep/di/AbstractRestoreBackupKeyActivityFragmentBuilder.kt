package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.factories.viewmodels.PassphraseRestoreBackupKeyFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.TextFileRestoreBackupKeyFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.PassphraseRestoreBackupKeyFragment
import vmodev.clearkeep.fragments.TextFileRestoreBackupKeyFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractPassphraseRestoreBackupKeyFragmentViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractTextFileRestoreBackupKeyFragmentViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractRestoreBackupKeyActivityFragmentBuilder {
    @ContributesAndroidInjector
    abstract fun contributePassphraseRestoreBackupKeyFragment(): PassphraseRestoreBackupKeyFragment;

    @Binds
    @Named(IFragment.PASSPHRASE_RESTORE_BACK_UP_KEY_FRAGMENT)
    abstract fun bindPassphraseRestoreBackupKeyFragment(fragment: PassphraseRestoreBackupKeyFragment): IFragment;

    @Binds
    abstract fun bindPassphraseRestoreBackupKeyFragmentViewModelFactory(factory: PassphraseRestoreBackupKeyFragmentViewModelFactory): IViewModelFactory<AbstractPassphraseRestoreBackupKeyFragmentViewModel>;

    @ContributesAndroidInjector
    abstract fun contributeTextFileRestoreBackupKeyFragment(): TextFileRestoreBackupKeyFragment;

    @Binds
    @Named(IFragment.TEXT_FILE_RESTORE_BACK_UP_KEY_FRAGMENT)
    abstract fun bindTextFileRestoreBackupKeyFragment(fragment: TextFileRestoreBackupKeyFragment): IFragment;

    @Binds
    abstract fun bindTextFileRestoreBackupKeyFragmentViewModelFactory(factory: TextFileRestoreBackupKeyFragmentViewModelFactory): IViewModelFactory<AbstractTextFileRestoreBackupKeyFragmentViewModel>;
}