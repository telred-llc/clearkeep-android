package vmodev.clearkeep.factories

import vmodev.clearkeep.factories.interfaces.IFragmentFactory
import vmodev.clearkeep.fragments.ContactsFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment

class ContactsFragmentFactory : IFragmentFactory {
    override fun createNewInstance(): IFragment {
        return ContactsFragment.newInstance();
    }
}