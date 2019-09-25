package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.activities.interfaces.IChangeThemeActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IChangeThemeActivityViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractChangeThemeActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class ChangeThemeActivityViewModelFactory @Inject constructor(@Named(IActivity.CHANGE_THEME_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : IChangeThemeActivityViewModelFactory {
    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractChangeThemeActivityViewModel::class.java);
    override fun getViewModel(): AbstractChangeThemeActivityViewModel {
        return viewModel;
    }
}