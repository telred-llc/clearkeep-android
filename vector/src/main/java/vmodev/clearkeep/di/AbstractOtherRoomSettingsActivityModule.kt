//package vmodev.clearkeep.di
//
//import dagger.Binds
//import dagger.Module
//import dagger.android.ContributesAndroidInjector
//import vmodev.clearkeep.fragments.OtherRoomSettingsFragment
//import vmodev.clearkeep.activities.interfaces.IActivity
//import vmodev.clearkeep.activities.interfaces.IOtherRoomSettingsActivity
//import javax.inject.Named
//
//@Module
//@Suppress("unused")
//abstract class AbstractOtherRoomSettingsActivityModule {
//    @ContributesAndroidInjector(modules = [ActivityBindModule::class])
//    abstract fun contributeOtherRoomSettingsActivity(): OtherRoomSettingsFragment;
//
//    @Module
//    abstract class ActivityBindModule {
//        @Binds
//        @Named(IActivity.OTHER_ROOM_SETTINGS_ACTIVITY)
//        abstract fun bindOtherRoomSettingsActivity(activity: OtherRoomSettingsFragment): IActivity;
//    }
//}