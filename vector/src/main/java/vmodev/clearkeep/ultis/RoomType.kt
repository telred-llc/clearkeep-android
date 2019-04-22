package vmodev.clearkeep.ultis

enum class RoomType(val value: Int) {
    DIRECT_MESSAGE(0b00000001),
    ROOM(0b00000010),
    FAVOURITE(0b10000000)
}