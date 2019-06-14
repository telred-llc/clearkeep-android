package vmodev.clearkeep.factories.activitiesandfragments

import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IFragmentFactory
import vmodev.clearkeep.fragments.FavouritesFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment

class FavouritesFragmentFactory : IFragmentFactory {
    override fun createNewInstance(): IFragment {
        return FavouritesFragment.newInstance();
    }
}