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
import java.lang.annotation.RetentionPolicy
import javax.inject.Named
import javax.inject.Scope

@Module
@Suppress("unused")
abstract class AbstractRoomSettingsActivityModule {
    @ContributesAndroidInjector(modules = [AbstractRoomSettingsActivityFragmentBuilderModule::class])
    abstract fun contributeRoomSettingsActivity(): RoomSettingsActivity;
//
//    @Binds
//    @Named("RoomSettingsActivity")
//    abstract fun bindRoomSettingActivity(activity: RoomSettingsActivity): FragmentActivity;
//
//    @Binds
//    @Named("RoomSettingsActivity")
//    abstract fun bindRoomSettingsDataBindingComponent(dataBindingCompomemt: ActivityDataBindingComponent): DataBindingComponent;
//    @Module
//    companion object {
//        @Provides
//        @JvmStatic
//        @Named(value = "RoomSettingsActivity")
//        fun provideDataBindingComponent(@Named(value = "RoomSettingsActivity") activity: IActivity): DataBindingComponent {
//            return ActivityDataBindingComponent(activity);
//        }
//    }
}