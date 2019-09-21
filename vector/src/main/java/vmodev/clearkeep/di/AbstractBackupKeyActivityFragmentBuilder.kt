package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.factories.viewmodels.BackupKeyManageFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.BackupKeyManageFragment
import vmodev.clearkeep.fragments.Interfaces.IBackupKeyManageFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractBackupKeyManageFragmentViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractBackupKeyActivityFragmentBuilder {
    @ContributesAndroidInjector(modules = [FragmentBackupKeyManageBindModule::class])
    abstract fun contributeBackupKeyManageFragment(): BackupKeyManageFragment;

    @Module
    abstract class FragmentBackupKeyManageBindModule {
        @Binds
        @Named(IFragment.BACKUP_KEY_MANAGE_FRAGMENT)
        abstract fun bindBackupKeyManageFragment(fragment: BackupKeyManageFragment): IFragment;

        @Binds
        abstract fun bindBackupKeyManageFragmentViewModelFactory(factory: BackupKeyManageFragmentViewModelFactory): IViewModelFactory<AbstractBackupKeyManageFragmentViewModel>;
    }
}