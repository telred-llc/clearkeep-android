package vmodev.clearkeep.factories.activitiesandfragments.interfaces

import vmodev.clearkeep.fragments.Interfaces.IFragment

interface IFragmentFactory {
    fun createNewInstance(): IFragment;

    companion object {
        const val HOME_SCREEN_FRAGMENT = "HOME_SCREEN_FRAGMENT";
        const val FAVOURITES_FRAGMENT = "FAVOURITES_FRAGMENT";
        const val CONTACTS_FRAGMENT = "CONTACTS_FRAGMENT";
        const val LIST_ROOM_FRAGMENT = "LIST_ROOM_FRAGMENT";
    }
}