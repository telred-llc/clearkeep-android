package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.IEditProfileActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IEditProfileActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractEditProfileActivityViewModel
import javax.inject.Inject

class EditProfileActivityViewModelFactory @Inject constructor(activity: IEditProfileActivity, factory: ViewModelProvider.Factory) : IEditProfileActivityViewModelFactory {
    private val viewModel: AbstractEditProfileActivityViewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractEditProfileActivityViewModel::class.java)
    override fun getViewModel(): AbstractEditProfileActivityViewModel {
        return viewModel;
    }
}