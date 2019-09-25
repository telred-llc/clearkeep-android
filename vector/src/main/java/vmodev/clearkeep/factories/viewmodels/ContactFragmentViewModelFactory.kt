package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.IContactFragmentViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IContactFragment
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractContactFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class ContactFragmentViewModelFactory @Inject constructor(@Named(IFragment.CONTACTS_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IContactFragmentViewModelFactory {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractContactFragmentViewModel::class.java);
    override fun getViewModel(): AbstractContactFragmentViewModel {
        return viewModel;
    }
}