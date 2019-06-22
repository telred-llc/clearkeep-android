package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import vmodev.clearkeep.activities.interfaces.IChangeThemeActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IChangeThemeActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractChangeThemeActivityViewModel
import javax.inject.Inject

class ChangeThemeActivityViewModelFactory @Inject constructor(activity: IChangeThemeActivity, factory: ViewModelProvider.Factory) : IChangeThemeActivityViewModelFactory {
    private val viewModel = ViewModelProviders.of(activity.getActivity(), factory).get(AbstractChangeThemeActivityViewModel::class.java);
    override fun getViewModel(): AbstractChangeThemeActivityViewModel {
        return viewModel;
    }
}