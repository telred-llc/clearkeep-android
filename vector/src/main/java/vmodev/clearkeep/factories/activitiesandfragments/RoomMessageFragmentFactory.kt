package vmodev.clearkeep.factories.activitiesandfragments

import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShowListRoomFragmentFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.Interfaces.ISearchFragment
import vmodev.clearkeep.fragments.Interfaces.ISearchRoomFragment
import vmodev.clearkeep.fragments.RoomFragment
import javax.inject.Inject

class RoomMessageFragmentFactory @Inject constructor() : IShowListRoomFragmentFactory {
    override fun createNewInstance(): ISearchRoomFragment {
        return RoomFragment.newInstance();
    }
}