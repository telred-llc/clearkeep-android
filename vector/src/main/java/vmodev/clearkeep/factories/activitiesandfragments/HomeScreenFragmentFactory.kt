package vmodev.clearkeep.factories.activitiesandfragments

import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IFragmentFactory
import vmodev.clearkeep.fragments.HomeScreenFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment

class HomeScreenFragmentFactory : IFragmentFactory {
    override fun createNewInstance(): IFragment {
        return HomeScreenFragment.newInstance();
    }
}