package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IMessageListActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IMessageListActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractMessageListActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class MessageListActivityViewModelFactory @Inject constructor(@Named(IActivity.MESSAGE_LIST_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : IMessageListActivityViewModelFactory {
    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractMessageListActivityViewModel::class.java);

    override fun getViewModel(): AbstractMessageListActivityViewModel {
        return viewModel;
    }
}