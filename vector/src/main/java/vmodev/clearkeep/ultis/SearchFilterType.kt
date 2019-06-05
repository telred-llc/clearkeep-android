package vmodev.clearkeep.ultis

enum class SearchFilterType(val value: UByte) {
    ROOM(0x01u),
    MESSAGE_BY_TEXT(0x02u),
}