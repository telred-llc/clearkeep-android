package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.LoginActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractLoginActivityModule {
    @ContributesAndroidInjector(modules = [AbstractLoginActivityFragmentBuilderModule::class])
    abstract fun contributeLoginActivity(): LoginActivity;

    @Binds
    @Named(IActivity.LOGIN_ACTIVITY)
    abstract fun bindLoginActivity(activity: LoginActivity): IActivity;
}