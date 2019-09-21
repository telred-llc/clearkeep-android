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
    @ContributesAndroidInjector(modules = [FragmentPassphraseRestoreBackupKeyBindModule::class])
    abstract fun contributePassphraseRestoreBackupKeyFragment(): PassphraseRestoreBackupKeyFragment;

    @ContributesAndroidInjector(modules = [FragmentTextFileRestoreBackupKeyBindModule::class])
    abstract fun contributeTextFileRestoreBackupKeyFragment(): TextFileRestoreBackupKeyFragment;

    @Module
    abstract class FragmentPassphraseRestoreBackupKeyBindModule {
        @Binds
        @Named(IFragment.PASSPHRASE_RESTORE_BACK_UP_KEY_FRAGMENT)
        abstract fun bindPassphraseRestoreBackupKeyFragment(fragment: PassphraseRestoreBackupKeyFragment): IFragment;

        @Binds
        abstract fun bindPassphraseRestoreBackupKeyFragmentViewModelFactory(factory: PassphraseRestoreBackupKeyFragmentViewModelFactory): IViewModelFactory<AbstractPassphraseRestoreBackupKeyFragmentViewModel>;
    }

    @Module
    abstract class FragmentTextFileRestoreBackupKeyBindModule {
        @Binds
        @Named(IFragment.TEXT_FILE_RESTORE_BACK_UP_KEY_FRAGMENT)
        abstract fun bindTextFileRestoreBackupKeyFragment(fragment: TextFileRestoreBackupKeyFragment): IFragment;

        @Binds
        abstract fun bindTextFileRestoreBackupKeyFragmentViewModelFactory(factory: TextFileRestoreBackupKeyFragmentViewModelFactory): IViewModelFactory<AbstractTextFileRestoreBackupKeyFragmentViewModel>;
    }
}