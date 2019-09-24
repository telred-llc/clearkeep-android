package vmodev.clearkeep.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import vmodev.clearkeep.applications.ClearKeepApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AndroidInjectionModule::class
    , AppModule::class, AbstractSplashActivityModule::class, AbstractHomeScreenActivityModule::class, AbstractProfileActivityModule::class
    , PreviewInviteRoomActivityModule::class
    , AbstractSearchActivityModule::class
    , AbstractRoomSettingsActivityModule::class
    , AbstractSecurityActivityModule::class
    , AbstractRoomMemberListActivityModule::class
    , AbstractRoomFilesListActivityModule::class
    , AbstractViewUserProfileActivityModule::class
    , AbstractOtherRoomSettingsAdvancedActivityModule::class
    , AbstractRolesPermissionActivityModule::class
    , AbstractEditProfileActivityModule::class
    , AbstractMessageListActivityModule::class
    , AbstractExportKeyActivityModule::class
    , AbstractUserInformationActivityModule::class
    , AbstractChangeThemeActivityModule::class
    , AbstractOtherRoomSettingsActivityModule::class
    , AbstractBackupKeyActivityModule::class
    , AbstractRestoreBackupKeyActivityModule::class
    , AbstractPushBackupKeyActivityModule::class
    , AbstractLoginActivityModule::class
    , AbstractRoomActivityModule::class
    , AbstractLogoutActivityModule::class
    , AbstractVectorMediaPickerActivityModule::class
    , AbstractNewRoomActivityModule::class
    , AbstractSettingsActivityModule::class])
interface AppComponent : AndroidInjector<ClearKeepApplication> {
    override fun inject(instance: ClearKeepApplication?)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: ClearKeepApplication): Builder

        fun build(): AppComponent
    }
}