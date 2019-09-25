package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.RestoreBackupKeyActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.RestoreBackupKeyActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractRestoreBackupKeyActivityViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractViewUserProfileActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractRestoreBackupKeyActivityModule {
    @ContributesAndroidInjector(modules = [AbstractRestoreBackupKeyActivityFragmentBuilder::class, ActivityBindModule::class])
    abstract fun contributeRestoreBackupKeyActivity(): RestoreBackupKeyActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.RESTORE_BACKUP_KEY)
        abstract fun bindRestoreBackupKeyActivity(activity: RestoreBackupKeyActivity): IActivity;

        @Binds
        abstract fun bindRestoreBackupKeyActivityViewModelFactory(factory: RestoreBackupKeyActivityViewModelFactory): IViewModelFactory<AbstractRestoreBackupKeyActivityViewModel>;
    }
}