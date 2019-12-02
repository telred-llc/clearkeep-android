package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.factories.viewmodels.interfaces.ICallHistoryViewModelFactory
import vmodev.clearkeep.factories.viewmodels.interfaces.IFeedBackViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractCallHistoryViewModel
import vmodev.clearkeep.viewmodels.interfaces.AbstractFeedBackFragmentViewModel
import javax.inject.Inject
import javax.inject.Named


class FeedBackFragmentViewModelFactory @Inject constructor(@Named(IFragment.FEED_BACK_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IFeedBackViewModelFactory {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractFeedBackFragmentViewModel::class.java)

    override fun getViewModel(): AbstractFeedBackFragmentViewModel {
        return viewModel
    }
}