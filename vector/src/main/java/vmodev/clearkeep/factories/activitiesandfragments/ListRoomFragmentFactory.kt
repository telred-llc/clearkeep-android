package vmodev.clearkeep.factories.activitiesandfragments

import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IFragmentFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.ListRoomFragment

class ListRoomFragmentFactory : IFragmentFactory {
    override fun createNewInstance(): IFragment {
        return ListRoomFragment.newInstance("");
    }
}