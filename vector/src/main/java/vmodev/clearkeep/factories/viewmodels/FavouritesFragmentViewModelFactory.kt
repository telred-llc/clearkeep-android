package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.IFavouritesFragmentViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFavouritesFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractFavouritesFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class FavouritesFragmentViewModelFactory @Inject constructor(@Named(IFragment.FAVOURITES_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IFavouritesFragmentViewModelFactory {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractFavouritesFragmentViewModel::class.java);
    override fun getViewModel(): AbstractFavouritesFragmentViewModel {
        return viewModel;
    }
}