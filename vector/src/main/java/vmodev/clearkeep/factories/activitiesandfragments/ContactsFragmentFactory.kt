package vmodev.clearkeep.factories.activitiesandfragments

import im.vector.Matrix
import vmodev.clearkeep.applications.IApplication
import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IFragmentFactory
import vmodev.clearkeep.fragments.ContactsFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment

class ContactsFragmentFactory constructor(private val application: IApplication) : IFragmentFactory {
    override fun createNewInstance(): IFragment {
        return ContactsFragment.newInstance(Matrix.getInstance(application.getApplication()).defaultSession.myUserId);
    }
}