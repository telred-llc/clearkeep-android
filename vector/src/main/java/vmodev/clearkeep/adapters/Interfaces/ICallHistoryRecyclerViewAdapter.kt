package vmodev.clearkeep.adapters.Interfaces

import vmodev.clearkeep.viewmodelobjects.MessageRoomUser
import vmodev.clearkeep.viewmodelobjects.RoomListUser

interface  ICallHistoryRecyclerViewAdapter{
    fun setOnItemClick(itemClick: (MessageRoomUser, Int) -> Unit?)
}