package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.factories.viewmodels.interfaces.IHomeScreenFragmentViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IHomeScreenFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractHomeScreenFragmentViewModel
import javax.inject.Inject

class HomeScreenFragmentViewModelFactory @Inject constructor(fragment: IHomeScreenFragment, factory: ViewModelProvider.Factory) : IHomeScreenFragmentViewModelFactory {
    private val viewModel = ViewModelProviders.of(fragment.getFragment(), factory).get(AbstractHomeScreenFragmentViewModel::class.java);
    override fun getViewModel(): AbstractHomeScreenFragmentViewModel {
        return viewModel;
    }
}