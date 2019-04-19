package vmodev.clearkeep.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import vmodev.clearkeep.viewmodelproviderfactories.ClearKeepViewModelProviderFactory
import vmodev.clearkeep.viewmodels.UserViewModel

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(userViewModel: UserViewModel): ViewModel;

    @Binds
    abstract fun bindViewModelFactory(clearKeepViewModelProviderFactory: ClearKeepViewModelProviderFactory): ViewModelProvider.Factory;
}