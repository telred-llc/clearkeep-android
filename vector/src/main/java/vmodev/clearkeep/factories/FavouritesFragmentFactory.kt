package vmodev.clearkeep.factories

import vmodev.clearkeep.factories.interfaces.IFragmentFactory
import vmodev.clearkeep.fragments.FavouritesFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment

class FavouritesFragmentFactory : IFragmentFactory {
    override fun createNewInstance(): IFragment {
        return FavouritesFragment.newInstance();
    }
}