package vmodev.clearkeep.enums

enum class EventTypeEnum(val value: String) {
    MISS_CALL("m.miss_call"),
    INVITE_TIMEOUT("invite_timeout"),
    IN_COMING_CALL("m.in_coming_call"),
    OUT_COMING_CALL("m.out_coming_call"),
    IMAGE("m.image"),
    FILE("m.file"),
    STICKER("m.sticker"),
    VIDEO("m.video"),
    AUDIO("m.audio"),
    TEXT("m.text")
}