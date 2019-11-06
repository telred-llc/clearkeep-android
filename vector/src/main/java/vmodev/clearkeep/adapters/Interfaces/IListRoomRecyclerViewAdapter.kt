package vmodev.clearkeep.adapters.Interfaces

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import vmodev.clearkeep.viewmodelobjects.RoomListUser

interface IListRoomRecyclerViewAdapter {
    fun setOnItemClick(itemClick: (RoomListUser, Int) -> Unit?)
    fun setOnItemLongClick(itemLongClick: (RoomListUser) -> Unit?)
    fun getAdapter(): ListAdapter<RoomListUser, *>
    fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner, currentUserId: String?);

    companion object {
        const val ROOM = "ROOM";
        const val ROOM_CONTACT = "ROOM_CONTACT";
        const val SHARE_FILE ="SHARE_FILE"
    }
}