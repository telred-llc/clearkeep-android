package vmodev.clearkeep.factories.activitiesandfragments

import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShareFileFragmentFactory
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShowListRoomFragmentFactory
import vmodev.clearkeep.fragments.DirectMessageFragment
import vmodev.clearkeep.fragments.DirectMessageShareFileFragment
import vmodev.clearkeep.fragments.Interfaces.IDirectMessageShareFileFragment
import vmodev.clearkeep.fragments.Interfaces.IRoomShareFileFragment
import vmodev.clearkeep.fragments.Interfaces.ISearchRoomFragment
import javax.inject.Inject

class DirectMessageShareFileFragmentFactory @Inject constructor() : IShareFileFragmentFactory {
    override fun createNewInstance(): IRoomShareFileFragment {
        return DirectMessageShareFileFragment.newInstance();
    }
}