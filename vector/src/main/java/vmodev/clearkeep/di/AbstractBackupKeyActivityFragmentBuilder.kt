package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.factories.viewmodels.BackupKeyManageFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.BackupKeyManageFragment
import vmodev.clearkeep.fragments.Interfaces.IBackupKeyManageFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractBackupKeyManageFragmentViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractBackupKeyActivityFragmentBuilder {
    @ContributesAndroidInjector
    abstract fun contributeBackupKeyManageFragment(): BackupKeyManageFragment;

    @Binds
    abstract fun bindBackupKeyManageFragment(fragment: BackupKeyManageFragment): IBackupKeyManageFragment;

    @Binds
    abstract fun bindBackupKeyManageFragmentViewModelFactory(factory: BackupKeyManageFragmentViewModelFactory): IViewModelFactory<AbstractBackupKeyManageFragmentViewModel>;
}