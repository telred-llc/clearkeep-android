package vmodev.clearkeep.factories

import vmodev.clearkeep.factories.interfaces.IShowListRoomFragmentFactory
import vmodev.clearkeep.fragments.DirectMessageFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment

class DirectMessageFragmentFactory : IShowListRoomFragmentFactory {
    override fun createNewInstance(): IFragment {
        return DirectMessageFragment.newInstance();
    }
}