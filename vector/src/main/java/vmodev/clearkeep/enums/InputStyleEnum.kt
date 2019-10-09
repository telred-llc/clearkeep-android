package vmodev.clearkeep.enums

enum class InputStyleEnum:IStyleInput {
    TEXT {
        override fun getStyleInput() = 0
    },
    PASSWORD {
        override fun getStyleInput() = 1
    },
    NUMBER {
        override fun getStyleInput() = 2
    },
    EMAIL {
        override fun getStyleInput() = 3
    },
    TEXT_MULTI {
        override fun getStyleInput() = 4
    }
}

interface IStyleInput {
    fun getStyleInput(): Int
}
