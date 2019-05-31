package vmodev.clearkeep.factories.activitiesandfragments

import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShowListRoomFragmentFactory
import vmodev.clearkeep.fragments.DirectMessageFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment

class DirectMessageFragmentFactory : IShowListRoomFragmentFactory {
    override fun createNewInstance(): IFragment {
        return DirectMessageFragment.newInstance();
    }
}