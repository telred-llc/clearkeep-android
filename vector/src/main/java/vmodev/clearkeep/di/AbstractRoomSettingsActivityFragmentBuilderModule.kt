package vmodev.clearkeep.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import vmodev.clearkeep.factories.viewmodels.RoomSettingsFragmentViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.*
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomSettingsFragmentViewModel
import javax.inject.Named

@Module
@Suppress("unused")
abstract class AbstractRoomSettingsActivityFragmentBuilderModule {

    @ContributesAndroidInjector(modules = [FragmentRoomSettingsBindModule::class])
    abstract fun contributeRoomSettingsFragment(): RoomSettingsFragment;

    @ContributesAndroidInjector(modules = [FragmentOtherRoomSettingsBindModule::class])
    abstract fun contributeOtherRoomSettingsFragment(): OtherRoomSettingsFragment;

    @ContributesAndroidInjector(modules = [FragmentRoomMemberListBindModule::class])
    abstract fun contributeRoomMemberListFragment(): RoomMemberListFragment;

    @ContributesAndroidInjector(modules = [AbstractNewRoomActivityBuilderFragmentBuilderModule.InviteUsersToRoomFragmentBindModule::class])
    abstract fun contributeInviteUsersToRoomFragment(): InviteUsersToRoomFragment;

    @ContributesAndroidInjector(modules = [FragmentOtherRoomSettingsAdvancedBindModule::class])
    abstract fun contributeOtherSettingsAdvanceFragment(): OtherRoomSettingsAdvancedFragment;

    @ContributesAndroidInjector(modules = [FragmentRolesPermissionBindModule::class])
    abstract fun contributeRolesPermissionFragment(): RolesPermissionFragment;

    @ContributesAndroidInjector(modules = [FragmentSecurityBindModule::class])
    abstract fun contributeSecurityFragment(): SecurityFragment;


    @Module
    abstract class FragmentRoomSettingsBindModule {
        @Binds
        @Named(IFragment.ROOM_SETTINGS_FRAGMENT)
        abstract fun bindRoomSettingsFragment(fragment: RoomSettingsFragment): IFragment;

        @Binds
        abstract fun bindRoomSettingsActivityViewModelFactory(factory: RoomSettingsFragmentViewModelFactory): IViewModelFactory<AbstractRoomSettingsFragmentViewModel>;
    }

    @Module
    abstract class FragmentOtherRoomSettingsBindModule {
        @Binds
        @Named(IFragment.OTHER_ROOM_SETTINGS_FRAGMENT)
        abstract fun bindOtherRoomSettingsFragment(fragment: OtherRoomSettingsFragment): IFragment;
    }

    @Module
    abstract class FragmentRoomMemberListBindModule {
        @Binds
        @Named(IFragment.ROOM_MEMBER_LIST_FRAGMENT)
        abstract fun bindRoomMemberListFragment(fragment: RoomMemberListFragment): IFragment;
    }

    @Module
    abstract class FragmentOtherRoomSettingsAdvancedBindModule {
        @Binds
        @Named(IFragment.OTHER_ROOM_SETTINGS_ADVANCE_FRAGMENT)
        abstract fun bindOtherRoomSettingsAdvancedFragment(fragment: OtherRoomSettingsAdvancedFragment): IFragment;
    }

    @Module
    abstract class FragmentRolesPermissionBindModule {
        @Binds
        @Named(IFragment.ROLES_PERMISSION_FRAGMENT)
        abstract fun bindRolesPermissionFragment(fragment: RolesPermissionFragment): IFragment;
    }

    @Module
    abstract class FragmentSecurityBindModule {
        @Binds
        @Named(IFragment.SECURITY_FRAGMENT)
        abstract fun bindSecurityPermissionFragment(fragment: SecurityFragment): IFragment;
    }

}