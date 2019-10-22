package vmodev.clearkeep.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import vmodev.clearkeep.applications.ClearKeepApplication
import vmodev.clearkeep.di.worker.AbstractWorkerModule
import vmodev.clearkeep.workermanager.ClearKeepWorkerFactory
import vmodev.clearkeep.workermanager.interfaces.IWorkerFactory
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AndroidInjectionModule::class, AbstractWorkerModule::class, AbstractWorkerModule.ClearKeepAssistedInjectModule::class
    , AppModule::class, AbstractSplashActivityModule::class, AbstractHomeScreenActivityModule::class, AbstractProfileActivityModule::class
    , PreviewInviteRoomActivityModule::class
    , AbstractSearchActivityModule::class
    , AbstractRoomSettingsActivityModule::class
    , AbstractRoomFilesListActivityModule::class
    , AbstractViewUserProfileActivityModule::class
    , AbstractEditProfileActivityModule::class
    , AbstractMessageListActivityModule::class
    , AbstractExportKeyActivityModule::class
    , AbstractUserInformationActivityModule::class
    , AbstractChangeThemeActivityModule::class
    , AbstractBackupKeyActivityModule::class
    , AbstractRestoreBackupKeyActivityModule::class
    , AbstractPushBackupKeyActivityModule::class
    , AbstractLoginActivityModule::class
    , AbstractRoomActivityModule::class
    , AbstractLogoutActivityModule::class
    , AbstractVectorMediaPickerActivityModule::class
    , AbstractNewRoomActivityModule::class
    , AbstractSettingsActivityModule::class
    , AbstractShareFileActivityModule::class
    , AbstractOutgoingCallActivityModule::class
    , AbstractRoomDetailActivityModule::class
    , AbstractIncomingCallActivityModule::class])
interface AppComponent : AndroidInjector<ClearKeepApplication> {
    override fun inject(instance: ClearKeepApplication?)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: ClearKeepApplication): Builder

        fun build(): AppComponent
    }

    fun workerFactory(): ClearKeepWorkerFactory;
}