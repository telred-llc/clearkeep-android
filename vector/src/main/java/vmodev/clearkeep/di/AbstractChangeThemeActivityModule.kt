package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.ChangeThemeActivity
import vmodev.clearkeep.activities.interfaces.IChangeThemeActivity
import vmodev.clearkeep.factories.viewmodels.ChangeThemeActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IChangeThemeActivityViewModelFactory

@Module
@Suppress("unused")
abstract class AbstractChangeThemeActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeChangeThemeActivity(): ChangeThemeActivity;

    @Binds
    abstract fun bindChangeThemeActivity(activity: ChangeThemeActivity): IChangeThemeActivity;

    @Binds
    abstract fun bindChangeThemeActivityViewModelFactory(factory: ChangeThemeActivityViewModelFactory): IChangeThemeActivityViewModelFactory;
}