package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.ICreateNewCallActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.ICreateNewCallActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractCreateNewCallActivityViewModel
import javax.inject.Inject

class CreateNewCallActivityViewModelFactory @Inject constructor(activity: ICreateNewCallActivity, factory: ViewModelProvider.Factory) : ICreateNewCallActivityViewModelFactory {

    private val viewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractCreateNewCallActivityViewModel::class.java);

    override fun getViewModel(): AbstractCreateNewCallActivityViewModel {
        return viewModel;
    }
}