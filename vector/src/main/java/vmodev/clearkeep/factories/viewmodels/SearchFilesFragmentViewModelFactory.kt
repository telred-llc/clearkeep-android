package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchFilesFragmentViewModel
import javax.inject.Inject
import javax.inject.Named

class SearchFilesFragmentViewModelFactory @Inject constructor(@Named(IFragment.SEARCH_FILES_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractSearchFilesFragmentViewModel> {
    private val viewModel = ViewModelProviders.of(fragment.getFragment(), factory).get(AbstractSearchFilesFragmentViewModel::class.java);
    override fun getViewModel(): AbstractSearchFilesFragmentViewModel {
        return viewModel;
    }
}