package vmodev.clearkeep.factories

import vmodev.clearkeep.factories.interfaces.IShowListRoomFragmentFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.RoomFragment

class RoomMessageFragmentFactory : IShowListRoomFragmentFactory {
    override fun createNewInstance(): IFragment {
        return RoomFragment.newInstance();
    }
}