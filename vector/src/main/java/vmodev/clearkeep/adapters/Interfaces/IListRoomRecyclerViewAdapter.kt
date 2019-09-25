package vmodev.clearkeep.adapters.Interfaces

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.RoomListUser
import vmodev.clearkeep.viewmodelobjects.User

interface IListRoomRecyclerViewAdapter {
    fun setOnItemClick(itemClick: (RoomListUser, Int) -> Unit?)
    fun setOnItemLongClick(itemLongClick: (RoomListUser) -> Unit?)
    fun getAdapter(): ListAdapter<RoomListUser, *>
    fun setCallbackToGetUsers(callback: ICallbackToGetUsers, lifecycleOwner: LifecycleOwner, currentUserId: String?);

    companion object {
        const val ROOM = "ROOM";
        const val ROOM_CONTACT = "ROOM_CONTACT";
    }

    interface ICallbackToGetUsers {
        fun getUsers(userIds: Array<String>): LiveData<Resource<List<User>>>;
    }
}