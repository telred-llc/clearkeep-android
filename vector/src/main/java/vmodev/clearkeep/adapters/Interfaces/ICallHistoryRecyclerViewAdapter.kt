package vmodev.clearkeep.adapters.Interfaces

import androidx.lifecycle.LifecycleOwner
import vmodev.clearkeep.viewmodelobjects.MessageRoomUser
import vmodev.clearkeep.viewmodelobjects.RoomListUser

interface  ICallHistoryRecyclerViewAdapter{
    fun setOnItemClick(itemClick: (MessageRoomUser, Int) -> Unit?)
    fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner, currentUserId: String?)
}