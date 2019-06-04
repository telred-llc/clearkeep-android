package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.factories.viewmodels.interfaces.IContactFragmentViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IContactFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractContactFragmentViewModel
import javax.inject.Inject

class ContactFragmentViewModelFactory @Inject constructor(fragment: IContactFragment, factory: ViewModelProvider.Factory) : IContactFragmentViewModelFactory {
    private val viewModel = ViewModelProviders.of(fragment.getFragment(), factory).get(AbstractContactFragmentViewModel::class.java);
    override fun getViewModel(): AbstractContactFragmentViewModel {
        return viewModel;
    }
}