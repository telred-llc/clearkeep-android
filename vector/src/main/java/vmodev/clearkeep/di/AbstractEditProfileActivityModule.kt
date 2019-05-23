package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.EditProfileActivity
import vmodev.clearkeep.activities.interfaces.IEditProfileActivity
import vmodev.clearkeep.factories.viewmodels.EditProfileActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IEditProfileActivityViewModelFactory

@Module
@Suppress("unused")
abstract class AbstractEditProfileActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeEditProfileActivity(): EditProfileActivity;

    @Binds
    abstract fun bindEditProfileActivity(activity: EditProfileActivity): IEditProfileActivity

    @Binds
    abstract fun bindEditProfileActivityViewModelFactory(factory: EditProfileActivityViewModelFactory): IEditProfileActivityViewModelFactory
}