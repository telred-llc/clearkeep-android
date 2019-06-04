package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.factories.viewmodels.interfaces.IDirectMessageFragmentViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IDriectMessageFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractDirectMessageFragmentViewModel
import javax.inject.Inject

class DirectMessageFragmentViewModelFactory @Inject constructor(fragment: IDriectMessageFragment, factory: ViewModelProvider.Factory) : IDirectMessageFragmentViewModelFactory {
    private val viewModel = ViewModelProviders.of(fragment.getFragment(), factory).get(AbstractDirectMessageFragmentViewModel::class.java)
    override fun getViewModel(): AbstractDirectMessageFragmentViewModel {
        return viewModel;
    }
}