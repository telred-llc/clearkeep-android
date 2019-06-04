package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.factories.viewmodels.interfaces.IFavouritesFragmentViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFavouritesFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractFavouritesFragmentViewModel
import javax.inject.Inject

class FavouritesFragmentViewModelFactory @Inject constructor(fragment: IFavouritesFragment, factory: ViewModelProvider.Factory) : IFavouritesFragmentViewModelFactory {
    private val viewModel = ViewModelProviders.of(fragment.getFragment(), factory).get(AbstractFavouritesFragmentViewModel::class.java);
    override fun getViewModel(): AbstractFavouritesFragmentViewModel {
        return viewModel;
    }
}