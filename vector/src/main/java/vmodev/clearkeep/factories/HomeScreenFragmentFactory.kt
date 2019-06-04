package vmodev.clearkeep.factories

import vmodev.clearkeep.factories.interfaces.IFragmentFactory
import vmodev.clearkeep.fragments.HomeScreenFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment

class HomeScreenFragmentFactory : IFragmentFactory {
    override fun createNewInstance(): IFragment {
        return HomeScreenFragment.newInstance();
    }
}