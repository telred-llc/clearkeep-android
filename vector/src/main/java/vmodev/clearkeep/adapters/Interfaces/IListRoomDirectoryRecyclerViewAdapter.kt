package vmodev.clearkeep.adapters.Interfaces

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom
import vmodev.clearkeep.viewmodelobjects.RoomListUser

interface IListRoomDirectoryRecyclerViewAdapter {
    fun setOnItemClick(itemClick: (PublicRoom, Int) -> Unit?)
    fun setOnItemLongClick(itemLongClick: (PublicRoom) -> Unit?)
    fun getAdapter(): ListAdapter<PublicRoom, *>
    fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner, currentUserId: String?);

    companion object {
        const val SEARCH_ROOMDIRECTORY = "SEARCH ROOM DIRECTORY"
    }
}