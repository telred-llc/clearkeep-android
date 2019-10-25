package vmodev.clearkeep.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import vmodev.clearkeep.viewmodelproviderfactories.ClearKeepViewModelProviderFactory
import vmodev.clearkeep.viewmodels.*
import vmodev.clearkeep.viewmodels.interfaces.*

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AbstractUserViewModel::class)
    abstract fun bindUserViewModel(userViewModel: UserViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractRoomViewModel::class)
    abstract fun bindRoomViewModel(roomViewModel: RoomViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractSearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractHomeScreenActivityViewModel::class)
    abstract fun bindHomeScreenActivityViewModel(viewModel: HomeScreenActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractDirectMessageFragmentViewModel::class)
    abstract fun bindDirectMessageFragmentViewModel(viewModel: DirectMessageFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractHomeScreenFragmentViewModel::class)
    abstract fun bindHomeScreenFragmentViewModel(viewModel: HomeScreenFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractRoomFragmentViewModel::class)
    abstract fun bindRoomFragmentViewModel(viewModel: RoomFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractFavouritesFragmentViewModel::class)
    abstract fun bindFavouritesFragmentViewModel(viewModel: FavouritesFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractContactFragmentViewModel::class)
    abstract fun bindContactFragmentViewModel(viewModel: ContactFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractProfileSettingsActivityViewModel::class)
    abstract fun bindProfileSettingsActivityViewModel(viewModel: ProfileSettingsActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractEditProfileActivityViewModel::class)
    abstract fun bindEditProfileActivityViewModel(viewModel: EditProfileActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractCallSettingActivityViewModel::class)
    abstract fun bindAbstractCallSettingActivityViewModel(view: CallSettingsActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractNotificationSettingsActivityViewModel::class)
    abstract fun bindAbstractNotificationSettingsActivityViewModel(viewModel: NotificationSettingsActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractMessageListActivityViewModel::class)
    abstract fun bindMessageListActivityViewModel(viewModel: MessageListActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractSplashActivityViewModel::class)
    abstract fun bindAbstractSplashActivityViewModel(viewModel: SplashActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractViewUserProfileActivityViewModel::class)
    abstract fun bindViewUserProfileActivityViewModel(viewModel: ViewUserProfileActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractExportKeyActivityViewModel::class)
    abstract fun bindExportKeyActivityViewModel(viewModel: ExportKeyActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractUserInformationActivityViewModel::class)
    abstract fun bindUserInformationActivityViewModel(viewModel: UserInformationActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractChangeThemeActivityViewModel::class)
    abstract fun bindChangeThemeActivityViewModel(viewModel: ChangeThemeActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractDataBindingDaggerActivityViewModel::class)
    abstract fun bindDataBindingDaggerActivityViewModel(viewModel: DataBindingDaggerActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractListRoomFragmentViewModel::class)
    abstract fun bindListRoomFragmentViewModel(viewModel: ListRoomFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractDeactivateAccountActivityViewModel::class)
    abstract fun bindDeactivateAccountActivityViewModel(viewModel: DeactivateAccountActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractReportActivityViewModel::class)
    abstract fun bindReportActivityViewModel(viewModel: ReportActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractCreateNewCallActivityViewModel::class)
    abstract fun bindCreateNewCallActivityViewModel(viewModel: CreateNewCallActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractRoomFileListActivityViewModel::class)
    abstract fun bindRoomFileListActivityViewModel(viewModel: RoomFileListActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractBackupKeyManageFragmentViewModel::class)
    abstract fun bindBackupKeyManageFragmentViewModel(viewModel: BackupKeyManageFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractRestoreBackupKeyActivityViewModel::class)
    abstract fun bindRestoreBackupKeyActivityViewModel(viewModel: RestoreBackupKeyActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractPassphraseRestoreBackupKeyFragmentViewModel::class)
    abstract fun bindPassphraseRestoreBackupKeyFragmentViewModel(viewModel: PassphraseRestoreBackupKeyFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractTextFileRestoreBackupKeyFragmentViewModel::class)
    abstract fun bindTextFileRestoreBackupKeyFragmentViewModel(viewModel: TextFileRestoreBackupKeyFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractPushBackupKeyActivityViewModel::class)
    abstract fun bindPushBackupKeyActivityViewModel(viewModel: PushBackupKeyViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractBackupKeyActivityViewModel::class)
    abstract fun bindBackupKeyActivityViewModel(viewModel: BackupKeyActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractProfileActivityViewModel::class)
    abstract fun bindProfileActivityViewModel(viewModel: ProfileActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractSearchActivityViewModel::class)
    abstract fun bindSearchActivityViewModel(viewModel: SearchActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractSearchMessageFragmentViewModel::class)
    abstract fun bindSearchMessageFragmentViewModel(viewModel: SearchMessageFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractSearchRoomsFragmentViewModel::class)
    abstract fun bindSearchRoomFragmentViewModel(viewModel: SearchRoomsFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractSearchPeopleFragmentViewModel::class)
    abstract fun bindSearchPeopleFragmentViewModel(viewModel: SearchPeopleFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractSearchFilesFragmentViewModel::class)
    abstract fun bindSearchFilesFragmentViewModel(viewModel: SearchFilesFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractCreateNewRoomActivityViewModel::class)
    abstract fun bindCreateNewRoomActivityViewModel(viewModel: CreateNewRoomActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractRoomSettingsFragmentViewModel::class)
    abstract fun bindRoomSettingsActivityViewModel(viewModel: RoomSettingsFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractFindAndCreateNewConversationActivityViewModel::class)
    abstract fun bindFindAndCreateNewConversationActivityViewModel(viewModel: FindAndCreateNewConversationActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractInviteUsersToRoomActivityViewModel::class)
    abstract fun bindInviteUsersToRoomActivityViewModel(viewModel: InviteUsersToRoomActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractIncomingCallFragmentViewModel::class)
    abstract fun bindIncomingCallFragmentViewModel(viewModel : IncomingCallFragmentViewModel) : ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractInProgressVoiceCallFragmentViewModel::class)
    abstract fun bindInProgressVoiceCallFragmentViewModel(viewModel : InProgressVoiceCallFragmentViewModel) : ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractOutgoingVoiceCallFragmentViewModel::class)
    abstract fun bindOutgoingVoiceCallFragmentViewModel(viewModel : OutgoingVoiceCallFragmentViewModel) : ViewModel;

    @Binds
    abstract fun bindViewModelFactory(clearKeepViewModelProviderFactory: ClearKeepViewModelProviderFactory): ViewModelProvider.Factory;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractRoomShareFileFragmentViewModel::class)
    abstract fun bindRoomShareFileFragmentViewModel(viewModel: RoomShareFileFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractDirectMessageShareFileFragmentViewModel::class)
    abstract fun bindDirectMessageShareFileFragmentViewModel(viewModel: DirectMessageShareFileFragmentViewModel): ViewModel;
}