package vmodev.clearkeep.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import vmodev.clearkeep.applications.ClearKeepApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AndroidInjectionModule::class
    , AppModule::class, AbstractSplashActivityModule::class, HomeScreenActivityModule::class, ProfileActivityModule::class
    , PreviewInviteRoomActivityModule::class, FindAndCreateNewConversationActivityModule::class
    , CreateNewRoomActivityModule::class
    , InviteUsersToRoomActivityModule::class
    , AbstractSearchActivityModule::class
    , AbstractRoomSettingsActivityModule::class
    , AbstractSecurityActivityModule::class
    , AbstractRoomMemberListActivityModule::class
    , AbstractRoomFilesListActivityModule::class
    , AbstractViewUserProfileActivityModule::class
    , AbstractOtherRoomSettingsAdvancedActivityModule::class
    , AbstractRolesPermissionActivityModule::class
    , AbstractProfileSettingsActivityModule::class
    , AbstractEditProfileActivityModule::class
    , AbstractCallSettingsActivityModule::class
    , AbstractNotificationSettingsActivityModule::class
    , AbstractMessageListActivityModule::class
    , AbstractNotificationSettingsActivityModule::class
    , AbstractExportKeyActivityModule::class
    , AbstractUserInformationActivityModule::class
    , AbstractChangeThemeActivityModule::class
    , AbstractOtherRoomSettingsActivityModule::class
    , AbstractDeactivateAccountActivityModule::class
    , AbstractPrivacyPolicyActivityModule::class
    , AbstractReportActivityModule::class
    , AbstractCreateNewCallActivityModule::class
    , AbstractCreateNewCallActivityModule::class
    , AbstractBackupKeyActivityModule::class
    , AbstractRestoreBackupKeyActivityModule::class
    , AbstractPushBackupKeyActivityModule::class
    , AbstractLoginActivityModule::class])
interface AppComponent : AndroidInjector<ClearKeepApplication> {
    override fun inject(instance: ClearKeepApplication?)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: ClearKeepApplication): Builder

        fun build(): AppComponent
    }
}