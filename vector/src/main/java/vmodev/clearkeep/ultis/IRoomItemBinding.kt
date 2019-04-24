package vmodev.clearkeep.ultis

import vmodev.clearkeep.viewmodelobjects.Room

interface IRoomItemBinding {
    fun customGetRoom(): Room?;
    fun customSetRoom(room: Room);
}