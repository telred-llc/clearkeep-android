package vmodev.clearkeep.factories.interfaces

import vmodev.clearkeep.fragments.Interfaces.IFragment

interface IShowListRoomFragmentFactory {
    fun createNewInstance(): IFragment;

    companion object {
        const val DIRECT_MESSAGE_FRAGMENT_FACTORY = "DIRECT_MESSAGE_FRAGMENT_FACTORY";
        const val ROOM_MESSAGE_FRAGMENT_FACTORY = "ROOM_MESSAGE_FRAGMENT_FACTORY";
    }
}
