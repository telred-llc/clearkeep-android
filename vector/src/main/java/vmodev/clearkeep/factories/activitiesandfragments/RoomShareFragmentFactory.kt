package vmodev.clearkeep.factories.activitiesandfragments

import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShareFileFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShowListRoomFragmentFactory
import vmodev.clearkeep.fragments.Interfaces.IRoomShareFileFragment
import vmodev.clearkeep.fragments.Interfaces.ISearchRoomFragment
import vmodev.clearkeep.fragments.RoomFragment
import vmodev.clearkeep.fragments.RoomShareFileFragment
import javax.inject.Inject

class RoomShareFragmentFactory @Inject constructor() : IShareFileFragmentFactory {
    override fun createNewInstance(): IRoomShareFileFragment {
        return RoomShareFileFragment.newInstance();
    }
}