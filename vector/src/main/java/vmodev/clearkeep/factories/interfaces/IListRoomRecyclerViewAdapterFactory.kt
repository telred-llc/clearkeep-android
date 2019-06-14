package vmodev.clearkeep.factories.interfaces

import android.databinding.DataBindingComponent
import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter
import vmodev.clearkeep.fragments.Interfaces.IListRoomOnFragmentInteractionListener

interface IListRoomRecyclerViewAdapterFactory {
    fun createAdapter(dataBindingComponent : DataBindingComponent) : IListRoomRecyclerViewAdapter;
}