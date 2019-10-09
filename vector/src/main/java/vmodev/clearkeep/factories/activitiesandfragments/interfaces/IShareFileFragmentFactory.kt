package vmodev.clearkeep.factories.activitiesandfragments.interfaces

import vmodev.clearkeep.fragments.Interfaces.IRoomShareFileFragment
import vmodev.clearkeep.fragments.Interfaces.ISearchRoomFragment

interface IShareFileFragmentFactory{
    fun createNewInstance(): IRoomShareFileFragment;

    companion object {
        const val DIRECT_MESSAGE_SHARE_FILE_FRAGMENT_FACTORY = "DIRECT_MESSAGE_SHARE_FILE_FRAGMENT_FACTORY";
        const val ROOM_MESSAGE_SHARE_FILE_FRAGMENT_FACTORY = "ROOM_MESSAGE_SHARE_FILE_FRAGMENT_FACTORY";
    }
}