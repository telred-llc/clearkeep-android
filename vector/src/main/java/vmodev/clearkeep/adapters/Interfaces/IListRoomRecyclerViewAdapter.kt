package vmodev.clearkeep.adapters.Interfaces

import android.databinding.DataBindingComponent
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import vmodev.clearkeep.viewmodelobjects.Room

interface IListRoomRecyclerViewAdapter {
    fun setOnItemClick(itemClick: (Room, Int) -> Unit?)
    fun setOnItemLongClick(itemLongClick: (Room) -> Unit?)
    fun getAdapter() : ListAdapter<Room,*>
    fun setdataBindingComponent(dataBindingComponent : DataBindingComponent)
}