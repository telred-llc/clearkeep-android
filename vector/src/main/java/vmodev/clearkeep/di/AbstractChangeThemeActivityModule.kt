package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.ChangeThemeActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IChangeThemeActivity
import vmodev.clearkeep.factories.viewmodels.ChangeThemeActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IChangeThemeActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractChangeThemeActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractChangeThemeActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeChangeThemeActivity(): ChangeThemeActivity;

    @Module
    abstract class ActivityBindModule {
        @Binds
        @Named(IActivity.CHANGE_THEME_ACTIVITY)
        abstract fun bindChangeThemeActivity(activity: ChangeThemeActivity): IActivity;

        @Binds
        abstract fun bindChangeThemeActivityViewModelFactory(factory: ChangeThemeActivityViewModelFactory): IViewModelFactory<AbstractChangeThemeActivityViewModel>;
    }
}