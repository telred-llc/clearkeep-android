package vmodev.clearkeep.factories.viewmodels

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import vmodev.clearkeep.factories.viewmodels.interfaces.IListRoomFragmentViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.IListRoomFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractListRoomFragmentViewModel
import javax.inject.Inject

class ListRoomFragmentViewModelFactory @Inject constructor(fragment: IListRoomFragment, factory: ViewModelProvider.Factory) : IListRoomFragmentViewModelFactory {
    private val viewModel = ViewModelProviders.of(fragment.getFragment(), factory).get(AbstractListRoomFragmentViewModel::class.java);
    override fun getViewModel(): AbstractListRoomFragmentViewModel {
        return viewModel;
    }
}