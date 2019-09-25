package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IProfileSettingsActivityViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractProfileSettingsActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class ProfileSettingsActivityViewModelFactory @Inject constructor(@Named(IFragment.PROFILE_SETTINGS_FRAGMENT) fragment: IFragment, factory: ViewModelProvider.Factory) : IProfileSettingsActivityViewModelFactory {
    private val viewModel = ViewModelProvider(fragment.getFragment(), factory).get(AbstractProfileSettingsActivityViewModel::class.java)
    override fun getViewModel(): AbstractProfileSettingsActivityViewModel {
        return viewModel;
    }
}