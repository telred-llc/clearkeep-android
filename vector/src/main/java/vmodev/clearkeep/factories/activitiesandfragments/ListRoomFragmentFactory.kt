package vmodev.clearkeep.factories.activitiesandfragments

import im.vector.Matrix
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IFragmentFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.ListRoomFragment

class ListRoomFragmentFactory constructor(private val application: IApplication) : IFragmentFactory {
    override fun createNewInstance(): IFragment {
        return ListRoomFragment();
    };
}