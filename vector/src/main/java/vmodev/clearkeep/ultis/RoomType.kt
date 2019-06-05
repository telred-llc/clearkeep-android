package vmodev.clearkeep.ultis

enum class RoomType(val value: UByte) {
    DIRECT_MESSAGE(0x01u),
    ROOM(0x02u),
    FAVOURITE(0x80u),
    INVITE(0x40u),
    ONLINE_STATUS(0x20u)
}