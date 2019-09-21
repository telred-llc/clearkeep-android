package vmodev.clearkeep.factories.interfaces

import vmodev.clearkeep.adapters.Interfaces.IListRoomRecyclerViewAdapter

interface IListRoomRecyclerViewAdapterFactory {
    fun createAdapter() : IListRoomRecyclerViewAdapter;
}