package vmodev.clearkeep.factories.activitiesandfragments.interfaces

import vmodev.clearkeep.fragments.Interfaces.ISearchRoomFragment

interface IShowListRoomFragmentFactory {
    fun createNewInstance(): ISearchRoomFragment;

    companion object {
        const val DIRECT_MESSAGE_FRAGMENT_FACTORY = "DIRECT_MESSAGE_FRAGMENT_FACTORY";
        const val ROOM_MESSAGE_FRAGMENT_FACTORY = "ROOM_MESSAGE_FRAGMENT_FACTORY";
    }
}
