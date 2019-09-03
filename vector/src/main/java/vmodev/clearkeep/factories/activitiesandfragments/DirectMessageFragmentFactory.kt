package vmodev.clearkeep.factories.activitiesandfragments

import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IShowListRoomFragmentFactory
import vmodev.clearkeep.fragments.DirectMessageFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.Interfaces.ISearchRoomFragment
import javax.inject.Inject

class DirectMessageFragmentFactory @Inject constructor() : IShowListRoomFragmentFactory {
    override fun createNewInstance(): ISearchRoomFragment {
        return DirectMessageFragment.newInstance();
    }
}