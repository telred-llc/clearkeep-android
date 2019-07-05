package vmodev.clearkeep.factories.activitiesandfragments

import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShowListRoomFragmentFactory
import vmodev.clearkeep.fragments.DirectMessageFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.Interfaces.ISearchRoomFragment

class DirectMessageFragmentFactory : IShowListRoomFragmentFactory {
    override fun createNewInstance(): ISearchRoomFragment {
        return DirectMessageFragment.newInstance();
    }
}