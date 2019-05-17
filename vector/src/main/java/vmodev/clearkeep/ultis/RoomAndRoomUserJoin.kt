package vmodev.clearkeep.ultis

import vmodev.clearkeep.viewmodelobjects.Room
import vmodev.clearkeep.viewmodelobjects.RoomUserJoin
import vmodev.clearkeep.viewmodelobjects.User

class RoomAndRoomUserJoin constructor(val room: Room, val users: List<User>, val roomUserJoins: List<RoomUserJoin>)