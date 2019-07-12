package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.BackupKeyActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IBackupKeyActivity
import vmodev.clearkeep.factories.viewmodels.BackupKeyActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractBackupKeyActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractBackupKeyActivityModule {

    @ContributesAndroidInjector(modules = [AbstractBackupKeyActivityFragmentBuilder::class])
    abstract fun contributeBackupKeyActivity(): BackupKeyActivity;

    @Binds
    @Named(IActivity.BACKUP_KEY)
    abstract fun bindBackupKeyActivity(activity: BackupKeyActivity): IActivity;

    @Binds
    abstract fun bindBackupKeyActivityViewModelFactory(factory: BackupKeyActivityViewModelFactory): IViewModelFactory<AbstractBackupKeyActivityViewModel>;
}