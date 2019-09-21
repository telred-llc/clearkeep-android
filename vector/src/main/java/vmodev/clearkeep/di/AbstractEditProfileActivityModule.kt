package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.EditProfileActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.EditProfileActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractEditProfileActivityViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractEditProfileActivityModule {
    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
    abstract fun contributeEditProfileActivity(): EditProfileActivity;

    @Module
    abstract class ActivityBindModule{
        @Binds
        @Named(IActivity.EDIT_PROFILE_ACTIVITY)
        abstract fun bindEditProfileActivity(activity: EditProfileActivity): IActivity

        @Binds
        abstract fun bindEditProfileActivityViewModelFactory(factory: EditProfileActivityViewModelFactory): IViewModelFactory<AbstractEditProfileActivityViewModel>
    }
}