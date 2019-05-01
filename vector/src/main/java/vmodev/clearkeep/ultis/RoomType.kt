package vmodev.clearkeep.ultis

enum class RoomType(val value: Int) {
    DIRECT_MESSAGE(0b00000001),
    ROOM(0b00000010),
    FAVOURITE(0b10000000),
    INVITE(0b01000000),
    ONLINE_STATUS(0b00100000)
}