package vmodev.clearkeep.adapters.Interfaces

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.databinding.DataBindingComponent
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import io.reactivex.Observable
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.User

interface IListRoomRecyclerViewAdapter {
    fun setOnItemClick(itemClick: (Room, Int) -> Unit?)
    fun setOnItemLongClick(itemLongClick: (Room) -> Unit?)
    fun getAdapter(): ListAdapter<Room, *>
    fun setDataBindingComponent(dataBindingComponent: DataBindingComponent)
    fun setCallbackToGetUsers(callback : ICallbackToGetUsers, lifecycleOwner: LifecycleOwner, currentUserId: String?);

    companion object {
        const val ROOM = "ROOM";
        const val ROOM_CONTACT = "ROOM_CONTACT";
    }

    interface ICallbackToGetUsers{
        fun getUsers(roomId : String) : LiveData<Resource<List<User>>>;
    }
}