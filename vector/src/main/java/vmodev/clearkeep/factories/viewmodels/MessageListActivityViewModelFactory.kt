package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.IMessageListActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IMessageListActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractMessageListActivityViewModel
import javax.inject.Inject

class MessageListActivityViewModelFactory @Inject constructor(activity: IMessageListActivity, factory: ViewModelProvider.Factory) : IMessageListActivityViewModelFactory {
    private val viewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractMessageListActivityViewModel::class.java);

    override fun getViewModel(): AbstractMessageListActivityViewModel {
        return viewModel;
    }
}