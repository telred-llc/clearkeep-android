package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.ShareFileActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractShareFileActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class, AbstractShareFileFragmentModule::class])
    abstract fun contributeShareFileActivity(): ShareFileActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.SHARE_FILE_ACTIVITY)
        abstract fun bindShareFileActivity(activity: ShareFileActivity): IActivity;
    }
}