package vmodev.clearkeep.enums

enum class TypeIconToolBar:ITypeIconToolBar {
    ICON_BACK {
        override fun getStyleInput() = 0
    },
    ICON_CLOSE {
        override fun getStyleInput() = 1
    },
}
interface ITypeIconToolBar  {
    fun getStyleInput(): Int
}
