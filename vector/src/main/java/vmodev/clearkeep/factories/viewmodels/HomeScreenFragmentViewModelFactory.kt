package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.IHomeScreenFragmentViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.fragments.Interfaces.IHomeScreenFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractHomeScreenFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class HomeScreenFragmentViewModelFactory @Inject constructor(@Named(IFragment.HOME_SCREEN_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IHomeScreenFragmentViewModelFactory {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractHomeScreenFragmentViewModel::class.java);
    override fun getViewModel(): AbstractHomeScreenFragmentViewModel {
        return viewModel;
    }
}