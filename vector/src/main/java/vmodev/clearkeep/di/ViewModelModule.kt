package vmodev.clearkeep.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import vmodev.clearkeep.viewmodelproviderfactories.ClearKeepViewModelProviderFactory
import vmodev.clearkeep.viewmodels.RoomViewModel
import vmodev.clearkeep.viewmodels.SearchViewModel
import vmodev.clearkeep.viewmodels.UserViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractUserViewModel

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
    abstract fun bindViewModelFactory(clearKeepViewModelProviderFactory: ClearKeepViewModelProviderFactory): ViewModelProvider.Factory;
}