package vmodev.clearkeep.factories.activitiesandfragments

import vmodev.clearkeep.factories.activitiesandfragments.interfaces.IFragmentFactory
import vmodev.clearkeep.fragments.ContactsFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment

class ContactsFragmentFactory : IFragmentFactory {
    override fun createNewInstance(): IFragment {
        return ContactsFragment.newInstance();
    }
}