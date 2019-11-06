package vmodev.clearkeep.adapters.Interfaces

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import vmodev.clearkeep.adapters.ListRoomWithStickyHeaderRecyclerViewAdapter
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodelobjects.User

interface IListRoomWithStickyHeaderRecyclerViewAdapter<T> {
    fun setOnItemClick(itemClick: (RoomListUser, Int) -> Unit?)
    fun setOnItemLongClick(itemLongClick: (RoomListUser) -> Unit?)
    fun setOnItemStickyHeaderClick(itemStickyHeaderClick: (Int) -> Unit?)
    fun getAdapter(): ListAdapter<RoomListUser, *>
    fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner, currentUserId: String?);
    fun setListHeader(listHeader: List<T>);
}