package vmodev.clearkeep.di

import android.databinding.DataBindingComponent
import android.support.v4.app.FragmentActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.activities.RoomSettingsActivity
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.binding.ActivityDataBindingComponent
import vmodev.clearkeep.di.scopes.RoomSettingsScope
import vmodev.clearkeep.factories.viewmodels.RoomSettingsActivityViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomSettingsActivityViewModel
import java.lang.annotation.RetentionPolicy
import javax.inject.Named
import javax.inject.Scope

@Module
@Suppress("unused")
abstract class AbstractRoomSettingsActivityModule {
    @ContributesAndroidInjector(modules = [AbstractRoomSettingsActivityFragmentBuilderModule::class])
    abstract fun contributeRoomSettingsActivity(): RoomSettingsActivity;

    @Binds
    @Named(IActivity.ROOM_SETTINGS_ACTIVITY)
    abstract fun bindRoomSettingsActivity(activity: RoomSettingsActivity): IActivity;

    @Binds
    abstract fun bindRoomSettingsActivityViewModelFactory(factory: RoomSettingsActivityViewModelFactory): IViewModelFactory<AbstractRoomSettingsActivityViewModel>;
}