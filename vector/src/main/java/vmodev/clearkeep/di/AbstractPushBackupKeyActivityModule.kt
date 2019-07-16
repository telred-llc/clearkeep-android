package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.PushBackupKeyActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.PushBackupKeyActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractPushBackupKeyActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractPushBackupKeyActivityModule {
    @ContributesAndroidInjector
    abstract fun contributePushBackupKeyActivity(): PushBackupKeyActivity;

    @Binds
    @Named(IActivity.PUSH_BACKUP_KEY)
    abstract fun bindPushBackupKeyActivity(activity: PushBackupKeyActivity): IActivity;

    @Binds
    abstract fun bindPushBackupKeyActivityViewModelFactory(factory: PushBackupKeyActivityViewModelFactory): IViewModelFactory<AbstractPushBackupKeyActivityViewModel>;
}