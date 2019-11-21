package vmodev.clearkeep.enums

enum class CallStatusEnum(val value: String) {
    MISS_CALL("m.miss_call"),
    IN_COMING_CALL("m.in_coming_call"),
    OUT_COMING_CALL("m.out_coming_call")
}