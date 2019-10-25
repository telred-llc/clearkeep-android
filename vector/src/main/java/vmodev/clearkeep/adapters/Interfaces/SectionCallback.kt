package vmodev.clearkeep.adapters.Interfaces

    interface SectionCallback {

        fun isSection(position: Int): Boolean

        fun getSectionHeader(position: Int): CharSequence
    }