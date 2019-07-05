package vmodev.clearkeep.factories.activitiesandfragments

import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShowListRoomFragmentFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.Interfaces.ISearchFragment
import vmodev.clearkeep.fragments.Interfaces.ISearchRoomFragment
import vmodev.clearkeep.fragments.RoomFragment

class RoomMessageFragmentFactory : IShowListRoomFragmentFactory {
    override fun createNewInstance(): ISearchRoomFragment {
        return RoomFragment.newInstance();
    }
}